package com.tktkgg.selfcontrol.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "icon", length = 30)
    private byte[] icon;

    @Column(name = "self_introduce", length = 500)
    private String selfIntroduce;

    public UUID getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public byte[] getIcon() {
        return this.icon;
    }

    public String getSelfIntroduce() {
        return this.selfIntroduce;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setSelfIntroduce(String selfIntroduce) {
        this.selfIntroduce = selfIntroduce;
    }
}
