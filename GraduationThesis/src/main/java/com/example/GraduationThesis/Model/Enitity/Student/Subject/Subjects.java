package com.example.GraduationThesis.Model.Enitity.Student.Subject;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subjects")
@Data
public class Subjects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Subject_ID")
    private Long id;

    @Column(name = "Subject_Name")
    private String name;

}