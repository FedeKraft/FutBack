package com.example.demo.domains.user.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)

    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column
    private String localidad;
    @Column
    private String playerAmount;
    @Column
    private String numero;

    public User(String name, String email, String password, String localidad, String playerAmount, String numero) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.localidad = localidad;
        this.playerAmount = playerAmount;
        this.numero = numero;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setPlayerAmount(String playerAmount) {
        this.playerAmount = playerAmount;
    }
    public String getPlayerAmount() {
        return playerAmount;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
