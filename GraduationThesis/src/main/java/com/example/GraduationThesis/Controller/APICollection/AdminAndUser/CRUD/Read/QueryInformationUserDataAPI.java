package com.example.GraduationThesis.Controller.APICollection.AdminAndUser.CRUD.Read;

import com.example.GraduationThesis.Service.API.InterfaceService.AdminAndUser.CRUD.Read.InformationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class QueryInformationUserDataAPI {
    @Autowired
    @Qualifier("QueryInformationUserDataImplementation")
    private InformationUser informationUser;

    @GetMapping("/myData")
    public ResponseEntity<?> queryInformationUserData() {
        return informationUser.queryInformationUserData();
    }
}
