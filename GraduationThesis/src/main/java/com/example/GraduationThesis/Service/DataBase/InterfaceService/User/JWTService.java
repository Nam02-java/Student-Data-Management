package com.example.GraduationThesis.Service.DataBase.InterfaceService.User;

import com.example.GraduationThesis.Model.Enitity.GlobalConfig.JWT;

import java.util.List;

public interface JWTService {

    List<JWT> findAll();
}
