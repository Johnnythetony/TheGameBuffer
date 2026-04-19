package com.liceolapaz.dam.proyectoev1di.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Entity
@Table(name="usuarios")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String mail;
    @Column(name="is_admin")
    private boolean admin;
    private boolean stylesheet;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Backlog> backlogEntradas;

    public User(){}

    public User(Long id, String username, String password, String mail, boolean admin, boolean stylesheet)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.admin = admin;
        this.stylesheet = stylesheet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean is_admin) {
        this.admin = is_admin;
    }

    public boolean isStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(boolean stylesheet) {
        this.stylesheet = stylesheet;
    }
}
