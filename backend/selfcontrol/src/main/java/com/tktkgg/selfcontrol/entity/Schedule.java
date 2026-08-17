package com.tktkgg.selfcontrol.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "schedules",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "day_of_week"}
    )
)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

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

    public DayOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public String getTitle() {
        return this.title;
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

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
