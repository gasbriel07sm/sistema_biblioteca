package com.unidevs.core_system;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por centralizar o tratamento de exceções (Controller Advice) na aplicação.
 * Fornece interceptação global para exceções específicas geradas durante a execução dos controladores REST.
 *
 * Trata exeções que ocorrem quando a validação de parâmetros de entrada (via Bean Validation) falha.
 * Tal mecanismo garante respostas HTTP padronizadas, melhorando a legibilidade e a manutenibilidade das
 * mensagens de erro.
 *
 * @ExceptionHandler
 * Captura exceções de validação (erros de constraint violation) e retorna um mapa contendo os campos
 * inválidos e suas respectivas mensagens de erro.
 *
 * Parâmetros:
 * @param ex Exceção lançada quando os argumentos de metodo anotados com @Valid falham na validação
 * @return {@link ResponseEntity} contendo um mapa JSON com chave (campo) e valor (mensagem de erro)
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
}