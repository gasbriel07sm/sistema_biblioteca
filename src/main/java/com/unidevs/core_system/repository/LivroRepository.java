package com.unidevs.core_system.repository;

import com.unidevs.core_system.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Repositório responsável pelo gerenciamento de operações de persistência da entidade {@link Livro}.
 *
 * Responsabilidade: interface que fornece automaticamente métodos CRUD e consultas personalizadas para a
 * manipulação de dados no banco de dados relacional. Atuando como a camada de acesso a dados
 *
 * Parâmetros:
 * @param titulo Parte do título a ser pesquisada.
 * @param autor Parte do nome do autor.
 * @param genero Parte do gênero literário.
 * @param tags Palavra-chave contida no campo de tags.
 * @param tag Tag a ser pesquisada.
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID> {
    List<Livro> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrGeneroContainingIgnoreCaseOrTagsContainingIgnoreCase(
            String titulo, String autor, String genero, String tags
    );
    List<Livro> findByTagsContainingIgnoreCase(String tag);

}