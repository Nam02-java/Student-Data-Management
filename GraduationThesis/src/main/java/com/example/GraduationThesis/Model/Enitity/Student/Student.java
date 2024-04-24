package com.example.GraduationThesis.Model.Enitity.Student;

import com.example.GraduationThesis.Model.Enitity.Student.Conduct.Conduct;
import com.example.GraduationThesis.Model.Enitity.Student.Subject.Scores;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "classname")
    private String classname;

    @Column(name = "email")
    private String email;

    @Column(name = "dateofbirth")
    private String dateOfBirth;

    @Column(name = "numberphone")
    private String numberphone;

    @Column(name = "address")
    private String address;

    @Column(name = "position")
    private String position;

    @Column(name = "teachername")
    private String teachername;

    @Column(name = "partentsname")
    private String partentsname;

    @Column(name = "partensnumberphone")
    private String partensnumberphone;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Scores> scores;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Conduct> conducts;
}

