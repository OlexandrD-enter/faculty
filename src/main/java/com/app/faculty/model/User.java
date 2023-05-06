package com.app.faculty.model;

import lombok.*;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String email;
    @Column(unique=true)
    private String username;
    private String password;
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
