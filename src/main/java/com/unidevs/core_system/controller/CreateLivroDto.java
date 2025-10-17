// Define que esta classe pertence ao pacote de controladores (onde os DTOs são colocados).
package com.unidevs.core_system.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Year;

/* DTO (Data Transfer Object) para a criação de um novo livro.
 * 'Public record' serve para criar classes simples transportadoras de dados, usando record, o Java cria automaticamente:
 * - Campos privados e finais (dados imutáveis).
 * - Um construtor que aceita todos os campos.
 * - Métodos 'get' para cada campo (ex: titulo(), autor()).
 * - Métodos 'equals()', 'hashCode()' e 'toString()' já implementados.
 * Ele economiza muito código em comparação com uma classe tradicional.*/

public record CreateLivroDto(
        // >>>> VALIDAÇÕES <<<<
        /*
        @NotBlank: serve para garantir que o título não seja nulo, nem uma string vazia ("")
             - message = "...": é a mensagem de erro customizada que será retornada se a validação falhar.
        @Size: serve para validar o tamanho da string.
             - min = 2: O título deve ter no mínimo 2 caracteres. (Indica o valor mínimo)
             - max = 100: O título deve ter no máximo 100 caracteres. (Indica o valor máximo)
        @NotNull: serve para garantir que o valor não seja nulo. É usado para objetos e tipos numéricos
        */

        @NotBlank(message = "O título não pode estar em branco.")
        @Size(min = 2, max = 100, message = "O título deve ter entre 2 e 100 caracteres.")
        String titulo,

        @NotBlank(message = "O autor não pode estar em branco.")
        @Size(min = 2, max = 100, message = "O nome do autor deve ter entre 2 e 100 caracteres.")
        String autor,

        // Campo opcional, sem validações.
        String genero,

        @NotNull(message = "O ano de publicação é obrigatório.")
        @Min(value = 1800, message = "O ano de publicação deve ser igual ou superior a 1800.")
        @Max(value = 2025, message = "O ano de publicação não pode ser no futuro.") // Use o ano atual aqui
        Integer anoPublicacao,

        @NotNull(message = "A quantidade disponível é obrigatória.")
        @Min(value = 0, message = "A quantidade disponível não pode ser negativa.")
        Integer quantidadeDisponivel,

        // Campo opcional, sem validações.
        String isbn,
        String tags
) {
    // >>>> CONSTRUTOR COM VALIDAÇÃO DINÂMICA <<<<
    // Possui a condição records, é executado antes do construtor principal para realizar validações ou normalização dos dados
    // Verifica se o ano de publicação não é maior que o ano atual, se for maior gera exceção e retorna erro HTTP 400 (Bad Request)
    public CreateLivroDto {
        if (anoPublicacao != null && anoPublicacao > Year.now().getValue()) {
            throw new IllegalArgumentException("O ano de publicação não pode ser no futuro.");
        }
    }
}