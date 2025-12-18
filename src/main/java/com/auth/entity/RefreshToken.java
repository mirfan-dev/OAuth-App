package com.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens" ,indexes = {

        @Index(name = "refresh_token_jti_index", columnList = "jti", unique = true),
        @Index(name = "refresh_token_user_id_index",columnList = "user_id")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "jti",unique = true,nullable = false,updatable = false)
    private String jti;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,updatable = false)
    private User user;

    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private Boolean revoked;

    private String replacedByToken;


}
