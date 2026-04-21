package com.petmediscan.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petmediscan.entity.Pet;
import com.petmediscan.form.PetCreateForm;
import com.petmediscan.service.PetService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;

    @GetMapping("")
    public String petList(HttpServletRequest request) {
        var token = request.getAttribute("token");//이렇게 오는지는 몰름;;
        return "pet_list";
    }

    @PostMapping("")
    public String createPet(@Valid PetCreateForm petCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create_pet";
        }

        String breed = petCreateForm.getBreed() == null ? "" : petCreateForm.getBreed();
        petService.create(petCreateForm.getName(), petCreateForm.getSpecies(), petCreateForm.getUser(), breed,
                petCreateForm.getBirth_date());

        return "user_detail";
    }

    //이하 readMe에는 없음
    @GetMapping("/modify/{petId}")
    public String modifyPet(PetCreateForm petCreateForm, @PathVariable("petId") Integer petId) {
        Pet pet = petService.get(petId);

        petCreateForm.setName(pet.getName());
        petCreateForm.setSpecies(pet.getSpecies());
        petCreateForm.setBreed(pet.getBreed());
        petCreateForm.setBirth_date(pet.getBirth_date());
        petCreateForm.setUser(pet.getUser());

        return "modify_pet";
    }

    @PostMapping("/modify/{id}")
    public String modifyPet(PetCreateForm petCreateForm, BindingResult bindingResult, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "modify_pet";
        }

        Pet pet = petService.get(id);

        petService.modify(pet, petCreateForm.getBreed(), petCreateForm.getBirth_date());

        return "user_detail";
    }
}