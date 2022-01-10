package com.example.recipe.app.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotNull(message = "can not be null")
    private String name;

    @Email(regexp = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$")
    @NotNull(message = "can not be null")
    private String email;
}
