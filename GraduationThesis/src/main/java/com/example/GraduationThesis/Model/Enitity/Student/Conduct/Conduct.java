package com.example.GraduationThesis.Model.Enitity.Student.Conduct;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "information_conduct")
@Data
public class Conduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Conduct_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Student_ID", referencedColumnName = "id")
    private Student student;

    @Column(name = "School_Year")
    private String school_year;

    @Column(name = "Conduct")
    private String conduct;

    @Column(name = "Attendance_Score")
    private String attendance_Score;

}
