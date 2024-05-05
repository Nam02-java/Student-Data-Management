package com.example.GraduationThesis.Controller.APICollection.Test;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class request {


    @NotNull(message = "Email is null")
    @NotBlank(message = "Email is blank")
    private String email;
    private String numberPhone;
}
