package com.example.GraduationThesis.Model.Repository.StudentRepository;

import com.example.GraduationThesis.Model.Enitity.Student.Subject.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subjects, Long> {

    Subjects findByName(String subjectName);

    Subjects getSubjectById(Long id);

    Subjects getSubjectByName(String subjectName);

}

