package com.unidevs.core_system.controller;

import com.unidevs.core_system.controller.dto.CreateLivroDto;
import com.unidevs.core_system.controller.dto.UpdateLivroDto;
import com.unidevs.core_system.entity.Livro;
import com.unidevs.core_system.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;

/**
 * Controlador que serve para gerenciar as operações relacionadas à entidade Livro
 *
 * Responsabilidades: expor endpoints REST para criação, listagem, busca, atualização e exclusão de livros, além de
 * funcionalidades específicas como empréstimo e reserva.
 *
 * Processo:
 * 1. Cadastro de livros com upload opcional de imagem de capa;
 * 2. Consulta individual e listagem completa de livros;
 * 3. Busca por título e por tags sem distinção de maiúsculas/minúsculas;
 * 4. Solicitação de empréstimo e reserva de livros via catálogo;
 * 5. Atualização de dados e imagem do livro;
 * 6. Exclusão lógica ou física do registro, conforme política do serviço.
 *
 * Respostas HTTP:
 *   201 Created: Livro criado com sucesso;
 *   200 OK: Operação realizada com sucesso (busca, reserva, empréstimo);
 *   204 No Content: Atualização ou exclusão bem-sucedida sem retorno de dados;
 *   400 Bad Request: Falha em requisição (ex: UUID inválido ou regra violada);
 *   404 Not Found: Livro não encontrado.
 */

@RestController
@RequestMapping("/livro")
public class LivroController {
    private final LivroService livroService;

    /**
     * Injeta a dependência da camada de serviço {@link LivroService}.
     * @param livroService Serviço responsável pelas regras de negócio e persistência de livros
     */
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    /**
     * createLivro() — Cria um novo registro de livro com ou sem imagem de capa.
     *
     * Processo:
     * 1. Valida os dados do DTO de criação;
     * 2. Chama o serviço para persistir o livro e armazenar a imagem (se houver);
     * 3. Retorna HTTP 201 Created com o location do recurso criado.
     *
     * Parâmetros:
     * @param createLivroDto DTO contendo os dados do livro;
     * @param imagemCapa Arquivo opcional de imagem da capa do livro;
     * @return Resposta HTTP com código 201 Created e header Location.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> createLivro(
            @Valid @RequestPart("livroDto") CreateLivroDto createLivroDto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemCapa) {
        var livroId = livroService.createLivro(createLivroDto, imagemCapa);
        return ResponseEntity.created(URI.create("/livro/" + livroId.toString())).build();
    }

    /**
     * getCatalogo() — Retorna uma lista de livros formatada para o catálogo.
     *
     * @return Lista de LivroCatalogoDto.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/catalogo")
    public ResponseEntity<List<com.unidevs.core_system.controller.dto.LivroCatalogoDto>> getCatalogo() {
        var livrosParaCatalogo = livroService.listarTodosParaCatalogo();
        return ResponseEntity.ok(livrosParaCatalogo);
    }

    /**
     * solicitarEmprestimo() — Solicita o empréstimo de um livro específico.
     *
     * Processo:
     * 1. Valida o UUID informado;
     * 2. Reduz a quantidade disponível no acervo;
     * 3. Retorna 200 OK se o empréstimo for bem-sucedido ou 400 Bad Request se falhar.
     *
     * @param livroId UUID do livro.
     * @return HTTP 200 em caso de sucesso ou 400 em caso de erro.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/catalogo/emprestimo/{livroId}")
    public ResponseEntity<Void> solicitarEmprestimo(@PathVariable("livroId") String livroId) {
        try {
            livroService.solicitarEmprestimo(livroId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * reservarLivro() — Realiza a reserva de um livro para o usuário.
     *
     * @param livroId UUID do livro a ser reservado.
     * @return HTTP 200 em caso de sucesso ou 400 em caso de falha.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/catalogo/reservar/{livroId}")
    public ResponseEntity<Void> reservarLivro(@PathVariable("livroId") String livroId) {
        try {
            livroService.reservarLivro(livroId);
            return ResponseEntity.ok().build(); // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * getLivroById() — Retorna um livro específico a partir do UUID informado.
     *
     * @param livroId UUID do livro.
     * @return Entidade {@link Livro} ou HTTP 404 se não encontrado.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{livroId}")
    public ResponseEntity<Livro> getLivroById(@PathVariable("livroId") String livroId) {
        var livro = livroService.getLivroById(livroId);
        return livro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * listLivros() — Retorna todos os livros cadastrados.
     *
     * @return Lista de {@link Livro}.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<Livro>> listLivros() {
        var livros = livroService.listLivros();
        return ResponseEntity.ok(livros);
    }

    /**
     * searchLivros() — Busca livros com base em um termo no título ou autor.
     *
     * @param termo Texto a ser buscado.
     * @return Lista de {@link Livro} que correspondem ao termo.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/buscar")
    public ResponseEntity<List<Livro>> searchLivros(@RequestParam("termo") String termo) {
        var livros = livroService.searchLivros(termo);
        return ResponseEntity.ok(livros);
    }

    /**
     * searchByTag() — Busca livros a partir de uma tag específica.
     *
     * @param tag Tag associada ao livro.
     * @return Lista de {@link Livro} com a tag informada.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/buscar-por-tag")
    public ResponseEntity<List<Livro>> searchByTag(@RequestParam("tag") String tag) {
        var livros = livroService.searchByTag(tag);
        return ResponseEntity.ok(livros);
    }

    /**
     * updateLivroById() — Atualiza os dados de um livro existente.
     *
     * Processo:
     * 1. Valida o DTO de atualização;
     * 2. Atualiza os campos alterados e a imagem (se houver);
     * 3. Retorna HTTP 204 No Content indicando sucesso.
     *
     * @param livroId UUID do livro.
     * @param updateLivroDto DTO com os novos dados.
     * @param imagemCapa Nova imagem opcional.
     * @return HTTP 204 No Content.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{livroId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> updateLivroById(
            @PathVariable("livroId") String livroId,
            @Valid @RequestPart("livroDto") UpdateLivroDto updateLivroDto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemCapa) {
        livroService.updateLivroById(livroId, updateLivroDto, imagemCapa);
        return ResponseEntity.noContent().build();
    }

    /**
     * deleteById() — Exclui um livro pelo UUID informado.
     *
     * @param livroId UUID do livro.
     * @return HTTP 204 No Content em caso de sucesso.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{livroId}")
    public ResponseEntity<Void> deleteById(@PathVariable("livroId") String livroId) {
        livroService.deleteById(livroId);
        return ResponseEntity.noContent().build();
    }
}