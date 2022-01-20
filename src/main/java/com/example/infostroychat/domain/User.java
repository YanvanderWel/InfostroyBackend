package com.example.infostroychat.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "isHandRaised")
    private boolean isHandRaised = false;

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHandRaised() {
        return isHandRaised;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isHandRaised=" + isHandRaised +
                '}';
    }

    public void setHandRaised(boolean handRaise) {
        isHandRaised = handRaise;
    }
}

