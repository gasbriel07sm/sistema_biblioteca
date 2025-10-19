package com.unidevs.core_system.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.Year;

/**
 * DTO responsável pela atualização parcial dos dados de um livro já existente.
 *
 * Responsabilidade: permitir a modificação apenas dos campos desejados, sem exigir o reenvio de todas as
 * informações originais.
 *
 * Processo:
 * A ausência de anotações @NotBlank ou @NotNull é intencional, pois permite que os campos não enviados pelo
 * cliente permaneçam inalterados na entidade persistida.
 *
 * Parâmetros
 * @param titulo Novo título (opcional, 2–100 caracteres).
 * @param autor  Novo autor (opcional, 2–100 caracteres).
 * @param genero Novo gênero (opcional).
 * @param anoPublicacao Novo ano de publicação (opcional, 1800 ≤ ano ≤ ano atual).
 * @param quantidadeDisponivel Nova quantidade disponível (opcional, ≥ 0).
 * @param isbn Novo código ISBN (opcional).
 * @param tags Novas palavras-chave (opcional).
 */
public record UpdateLivroDto(

        @Size(min = 2, max = 100, message = "O título deve ter entre 2 e 100 caracteres.")
        String titulo,

        @Size(min = 2, max = 100, message = "O nome do autor deve ter entre 2 e 100 caracteres.")
        String autor,

        String genero,

        @Min(value = 1800, message = "O ano de publicação deve ser igual ou superior a 1800.")
        @Max(value = 2025, message = "O ano de publicação não pode ser no futuro.")
        Integer anoPublicacao,

        @Min(value = 0, message = "A quantidade disponível não pode ser negativa.")
        Integer quantidadeDisponivel,

        String isbn,
        String tags
) {

    public UpdateLivroDto {
        if (anoPublicacao != null && anoPublicacao > Year.now().getValue()) {
            throw new IllegalArgumentException("O ano de publicação não pode ser no futuro.");
        }
    }
}