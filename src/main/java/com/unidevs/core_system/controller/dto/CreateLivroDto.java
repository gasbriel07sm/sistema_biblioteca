package com.unidevs.core_system.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Year;

/**
 * DTO responsável pela transferência de dados na operação de criação de um novo livro.
 *
 * Responsabilidades: receber e validar os dados enviados pelo cliente (API REST) antes de persistir a entidade no banco
 * de dados, garantindo a integridade e consistência das informações recebidas via requisições HTTP.
 *
 * Processo:
 * 1. Validação de campos obrigatórios e limites de tamanho.
 * 2. Uso do "record" para promover imutabilidade e segurança de dados.
 * 3. Validação dinâmica no construtor compacto, impedindo que o ano de publicação seja no futuro.
 *
 * Parâmetros:
 * @param titulo Título do livro. (Obrigatório, 2–100 caracteres);
 * @param autor Nome do autor. (Obrigatório, 2–100 caracteres);
 * @param genero Gênero literário. (Opcional);
 * @param anoPublicacao Ano de publicação. (Obrigatório, 1800 ≤ ano ≤ ano atual);
 * @param quantidadeDisponivel Quantidade disponível para empréstimo. (Obrigatória, ≥ 0);
 * @param isbn Identificador ISBN único do livro. (Opcional);
 * @param tags Conjunto de palavras-chave para classificação. (Opcional).
 */

public record CreateLivroDto(
        @NotBlank(message = "O título não pode estar em branco.")
        @Size(min = 2, max = 100, message = "O título deve ter entre 2 e 100 caracteres.")
        String titulo,

        @NotBlank(message = "O autor não pode estar em branco.")
        @Size(min = 2, max = 100, message = "O nome do autor deve ter entre 2 e 100 caracteres.")
        String autor,

        String genero,

        @NotNull(message = "O ano de publicação é obrigatório.")
        @Min(value = 1800, message = "O ano de publicação deve ser igual ou superior a 1800.")
        @Max(value = 2025, message = "O ano de publicação não pode ser no futuro.") // Use o ano atual aqui
        Integer anoPublicacao,

        @NotNull(message = "A quantidade disponível é obrigatória.")
        @Min(value = 0, message = "A quantidade disponível não pode ser negativa.")
        Integer quantidadeDisponivel,

        String isbn,
        String tags
) {

    /**
     * Construtor compacto que realiza validação dinâmica do campo 'anoPublicacao'
     *
     * A verificação é executada automaticamente no momento da criação do DTO.
     * Caso o ano informado seja maior que o ano atual, uma exceção é lançada (IllegalArgumentException), resultando em
     * resposta HTTP 400 (Bad Request).
     */
    public CreateLivroDto {
        if (anoPublicacao != null && anoPublicacao > Year.now().getValue()) {
            throw new IllegalArgumentException("O ano de publicação não pode ser no futuro.");
        }
    }
}