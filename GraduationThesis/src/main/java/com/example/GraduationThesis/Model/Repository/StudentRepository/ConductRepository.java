package com.example.GraduationThesis.Model.Repository.StudentRepository;

import com.example.GraduationThesis.Model.Enitity.Student.Conduct.Conduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConductRepository extends JpaRepository<Conduct, Long> {
    Conduct findByid(Long id);

}
