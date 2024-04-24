package com.example.GraduationThesis.Service.DataBase.ServiceImplements.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Conduct.Conduct;
import com.example.GraduationThesis.Model.Repository.StudentRepository.ConductRepository;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.ConductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConductServiceImplenment implements ConductService {

    @Autowired
    private ConductRepository conductRepository;

    public Long findByID(Long ID) {
        Conduct conduct = conductRepository.findByid(ID);
        if (conduct != null) {
            return conduct.getId();
        } else {
            return null;
        }
    }

}
