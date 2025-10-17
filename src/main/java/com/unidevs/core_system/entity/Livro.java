// Esta declaração define que a classe (Livro) pertence ao pacote de entidades do projeto
package com.unidevs.core_system.entity;

// Importa todas as classes e ferramentas necessárias.
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

// >>>> CLASSES <<<<
// Entity > serve para dizer como uma entidade deve ser mapeada para uma tabela no banco de dados
// Table > especifica o nome exato da tabela no banco de dados, garante mais controle a ela
@Entity
@Table(name = "Livros")
public class Livro {

// >>>> ATRIBUTOS <<<<
/* ‘ID’ > garante a CHAVE PRIMÁRIA (Primary Key) da tabela, a característica única que identifica
   GeneratedValue > diz como gerar o valor da chave primária, a estratégia usada UUID faz com que um identificador geral seja gerado automaticamente
   Column > permite customizar a coluna do banco de dados,
           | "nullable = false" - é uma restrição, onde a coluna não pode ser nula
           | "name = " - define o nome da coluna
           | "unique = true" - é uma restrição, garante que não pode existir o mesmo valor em dois itens na tabela, assim o banco rejeita
 */
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

    @Column(name = "isbn", unique = true)
    private String isbn;

    // Esta coluna serve para armazenar somente o nome do arquivo que vamos realizar o ‘upload’ da imagem, a imagem em si fica salva em uma pasta no servidor
    @Column(name = "caminho_imagem_capa")
    private String caminhoImagemCapa;

    // Serve para armazenar as tags que servem para referenciar os livros de maneira mais fácil em um único texto, separados por vírgula
    @Column(name = "tags")
    private String tags;

    // O Timestamp serve para preencher automaticamente com a data e hora atual, não precisando definir o valor manualmente
    @CreationTimestamp
    private Instant creationTimestamp;

    // No ‘Update’ ele faz com que toda hora que é autlizado e salvo um livro, este campo é atualizado automaticamente com a nova data e hora
    @UpdateTimestamp
    private Instant updateTimestamp;

    // >>>> CONSTRUTORES (sem parâmetro) <<<<
    // É necessário ter construtores para criar uma instância vazia do objeto antes de preenchê-la com dados vindos do banco de dados

    public Livro() {
    }

    // >>>> CONSTRUTORES (com parâmetro) <<<<
    // Esse serve para facilitar na criação de um novo objeto

    public Livro(String titulo, String autor, String genero, Integer anoPublicacao, Integer quantidadeDisponivel, String isbn, String caminhoImagemCapa, String tags) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.isbn = isbn;
        this.caminhoImagemCapa = caminhoImagemCapa;
        this.tags = tags;
    }

    // GETTERS / SETTERS
    // São métodos públicos para acessar (get) e modificar (set) od campos privados das classes, usam para ler e escrever os valores dos atributos do objeto

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