package com.example.GraduationThesis.Service.API.InterfaceService.User.CRUD.Read;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserServiceReadAPI {

    ResponseEntity<Object> queryGeneralInformationData();
    ResponseEntity<Object> queryPositionInClassData();
    ResponseEntity<Object> queryScoresForSubjectsData();
    ResponseEntity<Object> queryConductScoreData();
    ResponseEntity<Object> queryPersonalInformationData();
}
