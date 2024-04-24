package com.example.GraduationThesis.Controller.APICollection.Admin.CRUD.Read.Student;

import com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read.AdminServiceReadAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class QueryScoresForSubjectsDataAPI {
    @Autowired
    @Qualifier("QueryScoresForSubjectsDataImplenmentation")
    private AdminServiceReadAPI adminServiceReadAPI;

    @GetMapping("/queryScoresForSubjectsData")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Object> queryScoresForSubjectsData() {
        return adminServiceReadAPI.queryScoresForSubjectsData();
    }
}
