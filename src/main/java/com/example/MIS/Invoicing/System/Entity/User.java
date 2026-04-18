package com.example.MIS.Invoicing.System.Entity;

import jakarta.persistence.*;


@Entity
@Table(name="users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status Status;

    User(){

    }

    User(long id, String name, String email, String password, String role, Status Status){



        
    }
}