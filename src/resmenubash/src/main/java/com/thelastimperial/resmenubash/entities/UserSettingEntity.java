package com.thelastimperial.resmenubash.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="user_settings")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime credentialsExpiredAt;
    private LocalDateTime accountExpiredAt;
    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
