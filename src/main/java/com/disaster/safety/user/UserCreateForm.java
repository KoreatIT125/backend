package com.disaster.safety.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 6, max = 16)
    @NotEmpty(message = "ID를 입력해주시기 바랍니다.")
    private String username;

    @Size(min = 6, max = 16)
    @NotEmpty(message = "비밀번호를 입력해주시기 바랍니다.")
    private String password1;

    @Size(min = 6, max = 16)
    @NotEmpty(message = "비밀번호를 재입력해주시기 바랍니다.")
    private String password2;

    @Email
    @NotEmpty(message = "이메일을 입력해주시기 바랍니다.")
    private String email;
}