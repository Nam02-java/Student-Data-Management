package com.example.GraduationThesis.Service.DataBase.InterfaceService.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Subject.Subjects;

public interface SubjectService {

    Long findSubjectIdByName(String subjectName);

    Subjects getSubjectById(Long id);

    Subjects getSubjectByName(String subjectName);

}
