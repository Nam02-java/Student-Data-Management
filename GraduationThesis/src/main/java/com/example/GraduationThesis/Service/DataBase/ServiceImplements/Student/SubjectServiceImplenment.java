package com.example.GraduationThesis.Service.DataBase.ServiceImplements.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Subject.Subjects;
import com.example.GraduationThesis.Model.Repository.StudentRepository.SubjectRepository;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.Student.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImplenment implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // This method is used to find the ID of a subject based on its name
    public Long findSubjectIdByName(String subjectName) {
        Subjects subject = subjectRepository.findByName(subjectName);
        if (subject != null) {
            return subject.getId();
        } else {
            return null;
        }
    }

    @Override
    public Subjects getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Override
    public Subjects getSubjectByName(String subjectName) {
        return subjectRepository.findByName(subjectName);
    }
}
