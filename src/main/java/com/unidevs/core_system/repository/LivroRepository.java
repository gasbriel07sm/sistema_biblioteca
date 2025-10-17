package com.unidevs.core_system.repository;

import com.unidevs.core_system.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/* @Repository - ele marca a ‘interface’ como um "Repositório" do Spring
 * Isso significa que:
 *  1. O Spring a reconhece como um "Bean" (um objeto gerenciado por ele), permitindo que seja injetada em outras classes (como no LivroService)
 *  2. Habilita a tradução de exceções. Se ocorrer um erro no banco de dados (ex: SQLException), o Spring o converte para uma exceção mais genérica do Spring (DataAccessException), facilitando o tratamento de erros
 */

/* - extends JpaRepository<Livro, UUID> > Ao "estender" JpaRepository, nossa ‘interface’ LivroRepository HERDA um conjunto completo de métodos para manipular a entidade.
   - <Livro, UUID>: Estes são os "parâmetros genéricos"
     - Livro: Informa ao JpaRepository que esta ‘interface’ irá gerenciar a entidade 'Livro'
     - UUID: Informa o TIPO da chave primária (@Id) da entidade 'Livro'

Métodos criados:
1- save(Livro livro): Salva um livro novo ou atualiza um existente
2- findById(UUID id): Busca um livro pelo seu ID
3- findAll(): Retorna uma lista com TODOS os livros
4- deleteById(UUID id): Remove um livro pelo seu ID
*/

/* Um "Query Method" é um metodo que gera a consulta SQL correspondente automaticamente.
   - findBy: diz ao Spring para criar uma consulta
   - Titulo: procura no campo 'titulo' da entidade Livro
   - Containing: ele corresponde ao operador SQL 'LIKE' que procura por um texto que CONTENHA o valor fornecido
   - IgnoreCase: realiza a busca sem diferenciar maiúsculas de minúsculas
   - Or: É o operador lógico 'OU'. Ele conecta a próxima condição.
   - AutorContainingIgnoreCase: OU procura no campo 'autor'
   - GeneroContainingIgnoreCase: OU procura no campo 'genero'
   - TagsContainingIgnoreCase: OU procura no campo 'tags'

 * O Spring espera 4 parâmetros ‘String’, na ordem em que aparecem no nome do metodo
 * Este metodo alimenta a barra de busca principal da sua aplicação
 */

/* Um Query Method mais simples, usado para a busca específica quando o usuário clica em uma tag.
   - findBy: prefixo de consulta
   - Tags: serve para procurar no campo 'tags' da entidade
   - Containing: usa o operador 'LIKE'
   - IgnoreCase: ignora maiúsculas/minúsculas

 * O Spring espera um único parâmetro String, que será usado na condição 'LIKE'
 */

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID> {
    List<Livro> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrGeneroContainingIgnoreCaseOrTagsContainingIgnoreCase(
            String titulo, String autor, String genero, String tags
    );
    List<Livro> findByTagsContainingIgnoreCase(String tag);

}