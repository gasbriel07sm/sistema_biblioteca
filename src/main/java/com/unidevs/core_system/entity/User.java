package com.unidevs.core_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

// A implementação de 'UserDetails' e todos os imports de segurança foram removidos.
@Entity
@Table(name = "Usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID usuarioId;

    @Column(name = "usuarioRA", unique = true, nullable = false)
    private String usuarioRA;

    @Column(name = "usuarioNome")
    private String usuarioNome;

    @Column(name = "usuarioSenha")
    private String usuarioSenha;

    // O campo 'usuarioRole' foi removido, pois era apenas para segurança.

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    public User() {
    }

    public User(String usuarioRA, String usuarioNome, String usuarioSenha) {
        this.usuarioRA = usuarioRA;
        this.usuarioNome = usuarioNome;
        this.usuarioSenha = usuarioSenha;
    }

    // --- GETTERS E SETTERS ---
    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioRA() {
        return usuarioRA;
    }

    public void setUsuarioRA(String usuarioRA) {
        this.usuarioRA = usuarioRA;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioSenha() {
        return usuarioSenha;
    }

    public void setUsuarioSenha(String usuarioSenha) {
        this.usuarioSenha = usuarioSenha;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    // Todos os métodos @Override de UserDetails foram removidos.
}