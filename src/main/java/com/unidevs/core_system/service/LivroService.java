package com.unidevs.core_system.service;

import com.unidevs.core_system.controller.CreateLivroDto;
import com.unidevs.core_system.controller.UpdateLivroDto;
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

/* @Service: marca esta classe como um "Serviço" do Spring
   É a classe que contém a lógica de negócio principal da aplicação. O Spring a gerencia como um "Bean", permitindo que seja injetada em outras classes, como no LivroController
 */
@Service
public class LivroService {
    // Dependência da camada de repositório, o serviço precisa do repositório para interagir com o banco de dados
    private final LivroRepository livroRepository;
    // 'Path' é um objeto Java que representa um caminho de arquivo ou diretório
    // Neste caso, a variável armazenará o local onde as imagens de capa serão salvas
    private final Path fileStorageLocation;

    // Construtor para injeção de dependência, injeta o repository aqui e já configuramos o local de armazenamento de uploads
    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
        // Define o caminho para a pasta 'uploads' na raiz do projeto
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            // Garante que o diretório 'uploads' exista. Se não, ele o cria
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            // Se não for possível criar o diretório, a aplicação falha ao iniciar com uma mensagem clara
            throw new RuntimeException("Não foi possível criar o diretório para upload de arquivos.", ex);
        }
    }

    // Lógica para criar um novo livro → Orquestra a tarefa de salvar a imagem e depois salvar os dados do livro.
    public UUID createLivro(CreateLivroDto createLivroDto, MultipartFile imagemCapa) {
        // 1. Chama um metodo privado para salvar o arquivo de imagem e obtém seu nome
        String nomeArquivo = salvarImagem(imagemCapa);
        // 2. Cria uma instância da ENTIDADE Livro a partir dos dados do DTO
        // Os dados "de fora" (DTO) são convertidos para o formato "de dentro" (Entidade)
        var entity = new Livro(
                createLivroDto.titulo(),
                createLivroDto.autor(),
                createLivroDto.genero(),
                createLivroDto.anoPublicacao(),
                createLivroDto.quantidadeDisponivel(),
                createLivroDto.isbn(),
                nomeArquivo,
                createLivroDto.tags()
        );
        // 3. Pede ao repositório para salvar a entidade no banco de dados
        var livroSalvo = livroRepository.save(entity);
        // 4. Retorna o ID do livro recém-criado para o Controller
        return livroSalvo.getLivroId();
    }

    // Busca um livro pelo seu ID. Atua como um simples intermediário para o repositório
    //'Optional<Livro>' é uma forma segura de lidar com a possibilidade de o livro não ser encontrado
    public Optional<Livro> getLivroById(String livroId) {
        // Converte o ID de String para UUID antes de passar para o repositório
        return livroRepository.findById(UUID.fromString(livroId));
    }

    // Retorna a lista de todos os livros.
    public List<Livro> listLivros() {
        return livroRepository.findAll();
    }

    // Lógica para atualizar um livro existente.
    public void updateLivroById(String livroId, UpdateLivroDto updateLivroDto, MultipartFile imagemCapa) {
        var id = UUID.fromString(livroId);
        // 1. Busca o livro no banco de dados.
        var livroOptional = livroRepository.findById(id);

        // 2. Verifica se o livro foi encontrado.
        if (livroOptional.isPresent()) {
            var livro = livroOptional.get();

            // 3. Atualização parcial
            // Para cada campo, verifica se um novo valor foi enviado no DTO. Se sim, atualiza
            // Se o valor no DTO for nulo, o campo do livro original é mantido
            if (updateLivroDto.titulo() != null) livro.setTitulo(updateLivroDto.titulo());
            if (updateLivroDto.autor() != null) livro.setAutor(updateLivroDto.autor());
            if (updateLivroDto.genero() != null) livro.setGenero(updateLivroDto.genero());
            if (updateLivroDto.anoPublicacao() != null) livro.setAnoPublicacao(updateLivroDto.anoPublicacao());
            if (updateLivroDto.quantidadeDisponivel() != null) livro.setQuantidadeDisponivel(updateLivroDto.quantidadeDisponivel());
            if (updateLivroDto.isbn() != null) livro.setIsbn(updateLivroDto.isbn());
            if (updateLivroDto.tags() != null) livro.setTags(updateLivroDto.tags());

            // 4. Lógica de atualização da imagem.
            if (imagemCapa != null && !imagemCapa.isEmpty()) {
                // Se o livro já tinha uma imagem, apaga a antiga para não acumular lixo
                if (livro.getCaminhoImagemCapa() != null && !livro.getCaminhoImagemCapa().isEmpty()) {
                    deletarImagem(livro.getCaminhoImagemCapa());
                }
                // Salva a nova imagem e atualiza o caminho no objeto livro
                String novoNomeArquivo = salvarImagem(imagemCapa);
                livro.setCaminhoImagemCapa(novoNomeArquivo);
            }

            // 5. Salva o objeto 'livro' atualizado no banco de dados.
            livroRepository.save(livro);
        }
        // Se o livro não for encontrado, o metodo simplesmente termina sem fazer nada.
    }

    // Lógica para deletar um livro.
    public void deleteById(String livroId) {
        var id = UUID.fromString(livroId);
        // Busca o livro antes de deletar para poder apagar sua imagem associada
        livroRepository.findById(id).ifPresent(livro -> {
            // Se o livro tem uma imagem, chama o metodo para deletá-la do disco
            if (livro.getCaminhoImagemCapa() != null) {
                deletarImagem(livro.getCaminhoImagemCapa());
            }
            // Após lidar com a imagem, deleta o registro do livro no banco
            livroRepository.deleteById(id);
        });
    }

    // Lógica para a busca geral.
    public List<Livro> searchLivros(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return livroRepository.findAll();
        }
        // Delega a busca para o metodo complexo do repositório, passando o mesmo termo para todos os campos
        return livroRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCaseOrGeneroContainingIgnoreCaseOrTagsContainingIgnoreCase(
                termo, termo, termo, termo
        );
    }

    // Lógica para a busca por uma tag específica.
    public List<Livro> searchByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return livroRepository.findAll();
        }
        return livroRepository.findByTagsContainingIgnoreCase(tag.trim());
    }

    // Metodo auxiliar privado para salvar um arquivo de imagem no disco
    // 'private' significa que só pode ser chamado por outros métodos dentro desta mesma classe
    private String salvarImagem(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null; // Se nenhum arquivo foi enviado, não faz nada
        }
        // Limpa o nome do arquivo para evitar problemas de segurança (ex: "../../imagem.png")
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        // Extrai a extensão do arquivo (ex: ".png")
        if (originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        // Gera um nome de arquivo único usando UUID para evitar que dois uploads com o mesmo nome se sobrescrevam
        String storedFileName = UUID.randomUUID() + fileExtension;

        try {
            // Cria o caminho completo para o novo arquivo (ex: "/path/to/uploads/123-abc.png")
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            // Copia o conteúdo do arquivo enviado para o destino final no servidor
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            // Retorna o nome único gerado, que será salvo no banco de dados
            return storedFileName;
        } catch (IOException ex) {
            // Se ocorrer um erro de I/O (ex: disco cheio, sem permissão), lança uma exceção
            throw new RuntimeException("Não foi possível salvar o arquivo " + storedFileName, ex);
        }
    }

    // Metodo auxiliar privado para deletar um arquivo de imagem do disco
    private void deletarImagem(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            // Tenta deletar o arquivo se ele existir
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // Em caso de erro, apenas imprime no console, mas não para a execução da aplicação
            System.err.println("Não foi possível deletar o arquivo: " + filename);
        }
    }
}