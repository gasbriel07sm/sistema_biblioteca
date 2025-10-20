package com.unidevs.core_system.controller.dto;

import java.util.UUID;

/**
 * DTO utilizado para listar ou exibir informações resumidas de livros no catálogo.
 *
 * Responsabilidade: usado na apresentação do catálogo, ajuda no retorno de dados ao cliente, sem expor
 * informações internas da entidade "Livro".
 * Busca fornecer uma visualização leve e eficiente das informações essenciais para consultas e listagens
 * ideal para endpoints de catálogo ou páginas públicas.
 *
 * Parâmetros:
 * @param livroId Identificador único do livro (UUID).
 * @param titulo Título do livro.
 * @param autor Nome do autor.
 * @param caminhoImagemCapa Caminho ou URL da imagem de capa.
 * @param status Estado do livro (ex: "Disponível", "Emprestado", "Reservado").
 * @param quantidadeDisponivel Quantidade de exemplares disponíveis.
 * @param quantidadeTotal Quantidade total de exemplares cadastrados.
 */

public record LivroCatalogoDto(
        UUID livroId,
        String titulo,
        String autor,
        String caminhoImagemCapa,
        String status,
        Integer quantidadeDisponivel,
        Integer quantidadeTotal
) {
}