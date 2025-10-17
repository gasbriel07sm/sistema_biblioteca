package com.unidevs.core_system;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/* @ControllerAdvice: uma anotação poderosa do Spring, serve para transformar esta classe num componente global que pode interceptar e processar exceções de todos os @Controller e @RestController da aplicação. É uma forma de centralizar o tratamento de erros em um único lugar
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    /* @ExceptionHandler(MethodArgumentNotValidException.class): Esta anotação diz ao @ControllerAdvice:
     * "Execute este metodo SOMENTE QUANDO uma exceção do tipo 'MethodArgumentNotValidException' for lançada por qualquer controller."
     * Essa exceção específica é lançada pelo Spring automaticamente sempre que a validação de um parâmetro de metodo anotado com @Valid falha (como nos nossos DTOs)
     * --- @param contém todos os detalhes sobre os erros de validação.
     * --- @return Um ResponseEntity que representa a resposta HTTP completa (status, cabeçalhos e corpo).
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 1. Cria um mapa para armazenar os erros no formato: "nomeDoCampo" -> "mensagemDeErro"
        Map<String, String> errors = new HashMap<>();
        // 2. Acessa o resultado da validação a partir da exceção.
        //    'getBindingResult()' retorna um objeto que contém todos os erros encontrados.
        //    'getFieldErrors()' retorna uma lista específica de erros de validação de campos.
        ex.getBindingResult().getFieldErrors().forEach(error ->

                // 3. Para cada erro na lista
                // Pega o nome do campo que falhou na validação (ex: "titulo", "anoPublicacao")
                // Pega a mensagem de erro que definimos na anotação do DTO (ex: "O título não pode estar em branco.")
                // 4. Adiciona o par campo/erro ao nosso mapa
                errors.put(error.getField(), error.getDefaultMessage()));

        // 5. Retorna a resposta HTTP.
        //    ResponseEntity.badRequest() cria uma resposta com o status HTTP 400 (Bad Request)
        //    .body(errors) coloca o nosso mapa de erros no corpo da resposta
        //    O Spring irá converter o mapa Java para um objeto JSON automaticamente
        //    Exemplo do JSON retornado: { "titulo": "O título não pode estar em branco."}
        return ResponseEntity.badRequest().body(errors);
    }
}