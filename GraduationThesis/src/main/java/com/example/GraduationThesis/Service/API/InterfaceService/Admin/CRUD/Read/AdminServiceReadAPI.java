package com.example.GraduationThesis.Service.API.InterfaceService.Admin.CRUD.Read;

import java.util.List;

public interface AdminServiceReadAPI {

    List<Object> queryGeneralInformationData();

    List<Object> queryPositionInClassData();

    List<Object> queryScoresForSubjectsData();

    List<Object> queryConductScoreData();

    List<Object> queryPersonalInformationData();

    List<Object> queryUsersData();
}
