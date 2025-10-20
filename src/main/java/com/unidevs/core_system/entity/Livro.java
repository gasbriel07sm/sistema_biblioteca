package com.unidevs.core_system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidade Livro que representa um livro cadastrado no sistema.
 *
 * Responsabilidade: armazenar metadados e quantidades relativas ao acervo, além de congtrolar registros automáticos
 * de criação e atualização via Hibernate.
 *
 * Processo:
 * 1. Tabela mapeada: Livros;
 * 2. Identificador único global (UUID) gerado automaticamente;
 * 3. Controle de quantidade total e disponível;
 * 4. Registros automáticos de data/hora de criação e atualização.
 *
 * Parâmetros:
 * @param quantidadeTotal quantidade total;
 * @param updateTimestamp instante da última atualização;
 * @param livroId UUID a ser definido;
 * @param titulo título;
 * @param autor nome do autor;
 * @param genero gênero;
 * @param creationTimestamp instante de criação;
 * @param anoPublicacao ano de publicação;
 * @param quantidadeDisponivel quantidade disponível;
 * @param isbn ISBN;
 * @param caminhoImagemCapa nome do novo arquivo de imagem;
 * @param tags string de tags;
 * @param status status.
 */
@Entity
@Table(name = "Livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID livroId;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "genero")
    private String genero;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;

    @Column(name = "quantidade_total", nullable = false)
    private Integer quantidadeTotal;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Column(name = "caminho_imagem_capa")
    private String caminhoImagemCapa;

    @Column(name = "tags")
    private String tags;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    public Livro() {
    }

    // --- GETTERS E SETTERS ---

    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }
    public void setQuantidadeTotal(Integer quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public UUID getLivroId() {
        return livroId;
    }
    public void setLivroId(UUID livroId) {
        this.livroId = livroId;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }
    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Integer getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }
    public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCaminhoImagemCapa() {
        return caminhoImagemCapa;
    }
    public void setCaminhoImagemCapa(String caminhoImagemCapa) {
        this.caminhoImagemCapa = caminhoImagemCapa;
    }

    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
}