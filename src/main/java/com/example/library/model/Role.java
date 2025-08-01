package com.example.library.model;

import jakarta.persistence.*;

@Entity
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    // Getters and Setters
}
