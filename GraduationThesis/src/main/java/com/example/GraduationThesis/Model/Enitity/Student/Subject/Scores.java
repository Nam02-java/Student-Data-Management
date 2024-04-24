package com.example.GraduationThesis.Model.Enitity.Student.Subject;

import com.example.GraduationThesis.Model.Enitity.Student.Student;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "scores")
@Data
public class Scores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Score_ID")
    private Long scoreId;

    @ManyToOne
    @JoinColumn(name = "Student_ID", referencedColumnName = "id")
    private Student student;

    @Column(name = "Subject_ID")
    private long Subject_ID;

    @Column(name = "Score_15_Min")
    private String score15Min;

    @Column(name = "Score_1_Hour")
    private String score1Hour;

    @Column(name = "Score_Mid_Term")
    private String scoreMidTerm;

    @Column(name = "Score_Final_Exam")
    private String scoreFinalExam;

    @Column(name = "Score_Overall")
    private String scoreOverall;
}
