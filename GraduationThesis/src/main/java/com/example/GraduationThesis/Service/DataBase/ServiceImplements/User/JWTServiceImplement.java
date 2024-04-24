package com.example.GraduationThesis.Service.DataBase.ServiceImplements.User;

import com.example.GraduationThesis.Model.Enitity.GlobalConfig.JWT;
import com.example.GraduationThesis.Model.Repository.UserRepository.JWTRepository;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JWTServiceImplement implements JWTService {

    @Autowired
    private JWTRepository jwtRepository;


    @Override
    public List<JWT> findAll() {
        return jwtRepository.findAll();
    }
}
