package com.unidevs.core_system.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.Year;

/* DTO (Data Transfer Object) para a atualização de um novo livro.
 * 'Public record' serve para criar classes simples transportadoras de dados imutáveis e conciso
 * A diferença é que os seus campos são opcionais, permitindo atualizações parciais do livro. */

public record UpdateLivroDto(

        // A ausência de @NotBlank ou @NotNull é INTENCIONAL. Pois permite que o cliente não envie o campo 'titulo' se ele não quiser alterá-lo.
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
    // >>>> CONSTRUTOR VALIDAÇÃO DINÂMICA <<<<
    // Usado aqui para a mesma validação dinâmica do ano que em CreateLivroDto Ele verifica se o ano fornecido (se houver) não está no futuro.
    // Esta lógica só é executada se 'anoPublicacao' não for nulo. */

    public UpdateLivroDto {
        if (anoPublicacao != null && anoPublicacao > Year.now().getValue()) {
            throw new IllegalArgumentException("O ano de publicação não pode ser no futuro.");
        }
    }
}