package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Read.Student;

import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class QueryGeneralInformationDataAPI {

    @Autowired
    @Qualifier("QueryGeneralInformationDataImplenmentation")
    private AdminServiceReadAPI adminServiceReadAPI;

    @GetMapping("/queryGeneralInformationData")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Object> queryGeneralInformationData() {
        return adminServiceReadAPI.queryGeneralInformationData();
    }
}


