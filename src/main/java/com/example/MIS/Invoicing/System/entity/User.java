package com.example.MIS.Invoicing.System.entity;

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
    private Status status;

    User(){

    }

    User(long id, String name, String email, String password, String role, Status status){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        
    }
    //Getter

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Status getStatus() {
    return status;
    }

    //Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(Status status) {
    this.status = status;
    }
    

}