package com.unidevs.core_system.service;

import com.unidevs.core_system.controller.dto.LivroCatalogoDto;
import com.unidevs.core_system.controller.dto.CreateLivroDto;
import com.unidevs.core_system.controller.dto.UpdateLivroDto;
import com.unidevs.core_system.entity.Livro;
import com.unidevs.core_system.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors; // <<< NOVO

/**
 * Classe de serviço responsável pela orquestração das regras de negócio e operações de manipulação da entidade Livro.
 *
 * Responsabilidade: centralizar a lógica de persistência, validação e manipulação de dados, garantindo a
 * consistência e integridade da aplicação.
 *
 * Processos:
 * 1. Gerenciamento de livros (CRUD completo)
 * 2. Upload e exclusão de imagens de capa
 * 3. Controle de status de disponibilidade
 * 4. Consultas de livros por título, autor, gênero ou tags
 * 5. Processamento de empréstimos e reservas
 *
 * Parâmetros:
 * @param livroRepository Instância do LivroRepository
 * @param createLivroDto Dados do livro (DTO)
 * @param imagemCapa Arquivo de imagem opcional
 * @param livroId Identificador do livro
 * @param termo Termo de busca
 * @param tag Tag a ser pesquisada
 * @param livro Entidade Livro
 * @param file Arquivo enviado
 * @param filename Nome do arquivo
 *
 */
@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final Path fileStorageLocation;

    // Inicializa o repositório e configura o diretório de armazenamento de arquivos
    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para upload de arquivos.", ex);
        }
    }

    // Cria um novo livro e, opcionalmente, salva a imagem de capa
    public UUID createLivro(CreateLivroDto createLivroDto, MultipartFile imagemCapa) {
        String nomeArquivo = salvarImagem(imagemCapa);

        var entity = new Livro();
        entity.setTitulo(createLivroDto.titulo());
        entity.setAutor(createLivroDto.autor());
        entity.setGenero(createLivroDto.genero());
        entity.setAnoPublicacao(createLivroDto.anoPublicacao());
        entity.setQuantidadeDisponivel(createLivroDto.quantidadeDisponivel());
        entity.setQuantidadeTotal(createLivroDto.quantidadeDisponivel());
        entity.setIsbn(createLivroDto.isbn());
        entity.setCaminhoImagemCapa(nomeArquivo);
        entity.setTags(createLivroDto.tags());

        livroRepository.save(entity);

        if (entity.getQuantidadeDisponivel() > 0) {
            entity.setStatus("Disponível");
        } else {
            entity.setStatus("Emprestado");
        }

        var livroSalvo = livroRepository.save(entity);
        return livroSalvo.getLivroId();
    }

    // Retorna um livro pelo seu identificador
    public Optional<Livro> getLivroById(String livroId) {
        return livroRepository.findById(UUID.fromString(livroId));
    }

    // Retorna a lista completa de livros cadastrados
    public List<Livro> listLivros() {
        return livroRepository.findAll();
    }

    // Atualiza os dados de um livro existente, permitindo alteração dos campos.
    // Caso uma nova imagem seja enviada, a antiga é removida do diretório.
    public void updateLivroById(String livroId, UpdateLivroDto updateLivroDto, MultipartFile imagemCapa) {
        var id = UUID.fromString(livroId);
        var livroOptional = livroRepository.findById(id);

        if (livroOptional.isPresent()) {
            var livro = livroOptional.get();

            if (updateLivroDto.titulo() != null) livro.setTitulo(updateLivroDto.titulo());
            if (updateLivroDto.autor() != null) livro.setAutor(updateLivroDto.autor());
            if (updateLivroDto.genero() != null) livro.setGenero(updateLivroDto.genero());
            if (updateLivroDto.anoPublicacao() != null) livro.setAnoPublicacao(updateLivroDto.anoPublicacao());

            if (updateLivroDto.quantidadeDisponivel() != null) {
                livro.setQuantidadeDisponivel(updateLivroDto.quantidadeDisponivel());
                if (livro.getQuantidadeDisponivel() > 0) {
                    livro.setStatus("Disponível");
                } else {
                    livro.setStatus("Emprestado");
                }
            }

            if (updateLivroDto.isbn() != null) livro.setIsbn(updateLivroDto.isbn());
            if (updateLivroDto.tags() != null) livro.setTags(updateLivroDto.tags());

            if (imagemCapa != null && !imagemCapa.isEmpty()) {
                if (livro.getCaminhoImagemCapa() != null && !livro.getCaminhoImagemCapa().isEmpty()) {
                    deletarImagem(livro.getCaminhoImagemCapa());
                }
                String novoNomeArquivo = salvarImagem(imagemCapa);
                livro.setCaminhoImagemCapa(novoNomeArquivo);
            }

            livroRepository.save(livro);
        }
    }

    // Remove um livro e sua imagem de capa, caso exista
    public void deleteById(String livroId) {
        var id = UUID.fromString(livroId);
        livroRepository.findById(id).ifPresent(livro -> {
            if (livro.getCaminhoImagemCapa() != null) {
                deletarImagem(livro.getCaminhoImagemCapa());
            }
            livroRepository.deleteById(id);
        });
    }

    // Pesquisa livros por título, autor, gênero ou tags
    public List<Livro> searchLivros(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return livroRepository.findAll();
        }
        return livroRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrGeneroContainingIgnoreCaseOrTagsContainingIgnoreCase(
                termo, termo, termo, termo
        );
    }

    // Pesquisa livros que contenham determinada tag
    public List<Livro> searchByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return livroRepository.findAll();
        }
        return livroRepository.findByTagsContainingIgnoreCase(tag.trim());
    }

    // Retorna todos os livros convertidos para o formato de catálogo
    public List<LivroCatalogoDto> listarTodosParaCatalogo() {
        return livroRepository.findAll()
                .stream()
                .map(this::converterParaCatalogoDto)
                .collect(Collectors.toList());
    }

    // Processa o empréstimo de um livro
    // Reduz a quantidade disponível e atualiza o status.
    public void solicitarEmprestimo(String livroId) {
        var id = UUID.fromString(livroId);
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o ID: " + livroId));

        if (livro.getQuantidadeDisponivel() > 0) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);

            if (livro.getQuantidadeDisponivel() == 0) {
                livro.setStatus("Emprestado");
            }

            livroRepository.save(livro);
        } else {
            throw new RuntimeException("Não há exemplares disponíveis para empréstimo.");
        }
    }

    // Converte uma entidade Livro para o DTO de catálogo
    private LivroCatalogoDto converterParaCatalogoDto(Livro livro) {
        return new LivroCatalogoDto(
                livro.getLivroId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getCaminhoImagemCapa(),
                livro.getStatus(),
                livro.getQuantidadeDisponivel(),
                livro.getQuantidadeTotal()
        );
    }

    // Metodo placeholder para reserva de livros
    public void reservarLivro(String livroId) {
        var id = UUID.fromString(livroId);
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o ID: " + livroId));

        System.out.println("Endpoint de reserva para o livro '" + livro.getTitulo() + "' foi chamado. Lógica a ser implementada.");
    }

    // Salva a imagem enviada no diretório de uploads
    private String salvarImagem(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        if (originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String storedFileName = UUID.randomUUID() + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível salvar o arquivo " + storedFileName, ex);
        }
    }

    // Exclui uma imagem do diretório de uploads, caso exista
    private void deletarImagem(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            System.err.println("Não foi possível deletar o arquivo: " + filename);
        }
    }
}