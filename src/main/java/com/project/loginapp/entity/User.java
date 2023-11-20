package com.project.loginapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(length = 255)
    private String username;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String password;

}
