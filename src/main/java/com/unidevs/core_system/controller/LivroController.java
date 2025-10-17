// Esta declaração define que a classe (Livro) pertence ao pacote de controladores
package com.unidevs.core_system.controller;

// Importa todas as classes e ferramentas necessárias.
import com.unidevs.core_system.entity.Livro;
import com.unidevs.core_system.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;

// >>>> CLASSES <<<<
/* - @RestController: esta anotação combina duas outras: @Controller e @ResponseBody
 * - @Controller: marca a classe como um "controlador" do Spring MVC, que lida com requisições web
 * - @ResponseBody: diz que os retornos dos métodos desta classe devem ser convertidos para o corpo da resposta HTTP, em vez de tentar encontrar uma página HTML para renderizar
 * - @RequestMapping("/livro"): serve para definir o "endereço base" para todos os métodos dentro desta classe. T
*/

@RestController
@RequestMapping("/livro")
public class LivroController {

    // >>>> INJENÇÃO DE DEPENDÊNCIA <<<<
    // Define uma dependência de camada de serviço, o controler precisa do service para funcionar, o final garante que o service não pode ser trocado, uma vez que injetado
    private final LivroService livroService;

    // Construtor de uma classe, usa para realizar a Injenção de Dependência que serve para encontrar uma Bean automaticamente, onde a Bean é um obejto gerenciado pelo Spring do tipo LivroService, permite usar ele sem precisar criar uma instância
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    // >>>> ENDPOINTS (MÉTODOS QUE IRÃO LIDAR COM REQUISIÇÕES) <<<<
    /* - @PostMapping: serve para mapear as requisições HTTP do tipo POST, é a operação padrão para CRIAR um novo recurso
     * - consumes = {...}: serve para especificar que este endpoint espera dados no formato 'multipart/form-data', isto necessário para fazer upload de arquivos (a imagem da capa) com os dados (o DTO).
     * - @Valid @RequestPart("livroDto"): responsável por pegar a parte da requisição chamada "livroDto" (que é um JSON), e converter para um objeto CreateLivroDto e a VALIDA usando as anotações (@NotBlank, etc.).
     * - @RequestPart(value = "imagem", required = false): responsável por pegar a parte da requisição chamada "imagem" (que é o arquivo), a converte para um objeto MultipartFile. 'required = false' significa que o usuário não é obrigado a enviar uma imagem.
     */

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> createLivro(
            @Valid @RequestPart("livroDto") CreateLivroDto createLivroDto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemCapa) {
        // Serve para criar a lógica de criação para a camada de serviço
        var livroId = livroService.createLivro(createLivroDto, imagemCapa);
        // Retorna uma resposta HTTP 201 Created, onde o cabeçalho com "Location" define ao cliente a URL onde o novo livro pode ser encotrado
        return ResponseEntity.created(URI.create("/livro/" + livroId.toString())).build();
    }

    /* - @GetMapping("/{livroId}"): serve para mapear requisições HTTP GET para buscar UM recurso específico
     * - @PathVariable("livroId"): ele pega o valor da variável da URL e o passa para o parâmetro do metodo
     * - @RequestParam("termo"): serve para pegar o valor de um parâmetro da URL
     * - @PutMapping("/{livroId}"): serve para mapear as requisições HTTP PUT, usadas para ATUALIZAR um recurso existente por completo
     * - @DeleteMapping("/{livroId}"): Mapeia requisições HTTP DELETE, usadas para REMOVER um recurso
     */

    @GetMapping("/{livroId}")
    public ResponseEntity<Livro> getLivroById(@PathVariable("livroId") String livroId) {
        var livro = livroService.getLivroById(livroId);
        // Se o livro foi encontrado (isPresent), retorna um HTTP 200 OK com o livro no corpo.
        // Se não (isEmpty), retorna um HTTP 404 Not Found.
        return livro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listLivros() {
        var livros = livroService.listLivros();
        // Retorna HTTP 200 OK com a lista de livros (convertida para JSON) no corpo da resposta.
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Livro>> searchLivros(@RequestParam("termo") String termo) {
        var livros = livroService.searchLivros(termo);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/buscar-por-tag")
    public ResponseEntity<List<Livro>> searchByTag(@RequestParam("tag") String tag) {
        var livros = livroService.searchByTag(tag);
        return ResponseEntity.ok(livros);
    }

    @PutMapping(value = "/{livroId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> updateLivroById(
            @PathVariable("livroId") String livroId,
            @Valid @RequestPart("livroDto") UpdateLivroDto updateLivroDto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemCapa) {
        // Serve para delegar a lógica de atualização para o serviço.
        livroService.updateLivroById(livroId, updateLivroDto, imagemCapa);
        // Retorna HTTP 204 No Content, que é a resposta padrão para uma atualização bem-sucedida que não precisa retornar nenhum dado.
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{livroId}")
    public ResponseEntity<Void> deleteById(@PathVariable("livroId") String livroId) {
        livroService.deleteById(livroId);
        // Retorna HTTP 204 No Content, indicando sucesso na remoção
        return ResponseEntity.noContent().build();
    }
}