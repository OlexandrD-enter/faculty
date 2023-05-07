package com.app.faculty.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String topic;
    @Column(name = "date_start")
    private LocalDateTime dateStart;
    @Column(name = "date_end")
    private LocalDateTime dateEnd;
    @Column(name = "amount_students")
    private Long amountStudent;
    @ManyToOne
    @JoinColumn
    private Lecturer lecturer;
}
