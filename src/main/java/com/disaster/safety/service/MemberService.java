package com.disaster.safety.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.disaster.safety.dto.CustomUserInfoDto;
import com.disaster.safety.dto.LoginRequestDto;
import com.disaster.safety.entity.Member;
import com.disaster.safety.exception.ValidateMemberException;
import com.disaster.safety.repository.MemberRepository;
import com.disaster.safety.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Transactional
    public String login(LoginRequestDto dto) {
        String password = dto.getPassword();
        String userId = dto.getUserId();
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);

        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("User not found.");
        }

        Member member = optionalMember.get();

        if (!encoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("Password does not match.");
        }

        CustomUserInfoDto info = CustomUserInfoDto.builder()
                .memberId(member.getId())
                .userId(member.getUserId())
                .password(member.getPassword())
                .userName(member.getUserName())
                .role(member.getRole())
                .build();
        return jwtUtil.createAccessToken(info);
    }

    @Transactional
    public Long signup(Member member) {
        Optional<Member> validMember = memberRepository.findByUserId(member.getUserId());

        if (validMember.isPresent()) {
            throw new ValidateMemberException("User already exists.");
        }

        member.updatePassword(encoder.encode(member.getPassword()));
        memberRepository.create(member);
        return member.getId();
    }
}
