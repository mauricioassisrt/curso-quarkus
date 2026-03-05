package br.com.projeto.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="followers")
@Data
public class FollowersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserEntity usuario;
    @ManyToOne
    @JoinColumn(name = "followers_id", nullable = false)
    private UserEntity followers;

 }
