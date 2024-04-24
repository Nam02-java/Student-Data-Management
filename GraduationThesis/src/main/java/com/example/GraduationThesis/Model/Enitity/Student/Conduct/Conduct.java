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

    @Column(name = "Conduct2017_2018")
    private String conduct2017_2018;

    @Column(name = "Conduct2018_2019")
    private String conduct2018_2019;

    @Column(name = "Conduct2019_2020")
    private String conduct2019_2020;

    @Column(name = "Attendance_Score")
    private String attendance_Score;

}
