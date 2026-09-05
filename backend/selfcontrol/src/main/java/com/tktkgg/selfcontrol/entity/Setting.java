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
@Table(name = "settings")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete (action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "is_authorize_notification")
    private boolean isAuthorizeNotification;

    public UUID getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }
    
    public boolean getIsPublic() {
        return this.isPublic;
    }

    public boolean getIsAuthorizeNotification() {
        return this.isAuthorizeNotification;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setIsAuthorizeNotification(boolean isAuthorizeNotification) {
        this.isAuthorizeNotification = isAuthorizeNotification;
    }
}
