package com.tktkgg.selfcontrol.entity;

import java.util.UUID;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Column;

@Entity
@Table(
    name = "likes",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_likes_user_target",
        columnNames = { "user_id", "target_user_id" }
    ),
    check = @CheckConstraint(
        name = "ck_likes_not_self",
        constraint = "user_id <> target_user_id"
    )
)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User targetUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public User getTargetUser() {
        return this.targetUser;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
