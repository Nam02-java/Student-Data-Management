package com.example.GraduationThesis.Model.PayLoad.User.UpdateUser;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateUserRequest {

    private Long id;
    private Map<String, String> updates;

    private String email;

    private String numberphone;

    private String username;

    private String password;
}
