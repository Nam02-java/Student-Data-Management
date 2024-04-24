package com.example.GraduationThesis.Controller.APICollection.User.CRUD.Read;

import com.example.GraduationThesis.Service.API.InterfaceService.User.CRUD.Read.UserServiceReadAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserQueryConductScoreDataAPI {
    @Autowired
    @Qualifier("UserQueryConductScoreDataImplenmentation")
    private UserServiceReadAPI userServiceReadAPI;

    @GetMapping("/queryConductScoreData")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> queryConductScoreData() {
        return userServiceReadAPI.queryConductScoreData();
    }
}
