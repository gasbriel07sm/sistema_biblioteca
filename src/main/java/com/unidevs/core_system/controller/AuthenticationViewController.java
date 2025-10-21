package com.unidevs.core_system.controller;

import com.unidevs.core_system.entity.User;
import com.unidevs.core_system.entity.UserRole;
import com.unidevs.core_system.repository.UserRepository;
import com.unidevs.core_system.security.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Authentication Controller para Interface Web
 *
 * Este controlador lida com a autenticação e registro de usuários através
 * de uma interface web tradicional, todavia, implementa autenticação STATELESS usando
 * tokens JWT.
 *
 * Serve como a ponte entre a UI e a infraestrutura Spring Security.
 *
 * O controlador implementa STATELESS usando:
 * - Tokens JWT ao invés de sessões server-side;
 * - Armazenamento de Cookies para manter a autenticação entre redirects;
 * - Atributos Flash para transmitir informações temporárias do usuário entre requests;
 *
 * Componentes:
 * - AuthenticationManager: Valida as credenciais fornecidas com o banco;
 * - TokenService: Gera Tokens JWT contendo a identidade do usuário e roles;
 * - UserRepository: Acesso direto ao banco para validação e registro de usuários;
 * - PasswordEncoder: Gera hash para armazenamento de senhas no banco.
 *
 * Este controlador funciona em conjunto com SecurityConfigurations e o SecurityFilter.
 */

@Controller
public class AuthenticationViewController {

    // Dependências injetadas através do construtor.
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationViewController(AuthenticationManager authManager,
                                        TokenService tokenService,
                                        UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * processLogin() Lida com submissões do formulário de Login
     *
     * Processo:
     * 1. Tenta autenticar as credenciais com o Spring Security;
     * 2. Se sucesso, gera um token JWT contendo a identidade do usuário;
     * 3. Armazena o token em um cookie para manter autenticação entre redirects;
     * 4. Define atributos Flash para informações do usuário depois de um redirect;
     * 5. Redirect para a home page;
     * 6. Se falha, retorna para a login page com mensagem de erro.
     *
     * @param login Login enviado do formulário;
     * @param password Senha enviado do formulário;
     * @param response Objeto HTTP response para definir cookies;
     * @param redirectAttributes Objeto para passar dados entre redirects;
     * @param model Container de dados para renderização de páginas
     * @return Nome da página para renderização
     */
    @PostMapping("/login-process")
    public String processLogin(@RequestParam String login,
                               @RequestParam String password,
                               HttpServletResponse response,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        try {
            // Autenticar usuário
            var authToken = new UsernamePasswordAuthenticationToken(login, password);
            var auth = authManager.authenticate(authToken);

            // Gerar JWT token
            var user = (User) auth.getPrincipal();
            var token = tokenService.generateToken(user);

            // Adicionar token como cookie para manter autenticação entre redirects
            Cookie jwtCookie = new Cookie("jwt_token", token);
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(false); // Permitir acesso JavaScript
            jwtCookie.setMaxAge(7200); // 2 horas
            response.addCookie(jwtCookie);

            // Definir informações do usuário como Flash
            redirectAttributes.addFlashAttribute("userRole", user.getRole().toString());
            redirectAttributes.addFlashAttribute("userName", user.getLogin());

            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Credenciais inválidas");
            return "login/login";
        }
    }

    /**
     * processRegistration() Lida com submissões do formulário de Cadastro
     *
     * Processo:
     * 1. Verifica se o Login já existe no sistema;
     * 2. Se Login já existe, retorna a página de registro com erro;
     * 3. Se Login está disponível, cria novo usuário com:
     *  - Login e Email;
     *  - Senha Hashada;
     *  - Role padrão de usuário;
     * 4. Salva o usuário no banco
     * 5. Retorna para a página de login com sucesso;
     *
     * @param login Login do usuário;
     * @param email Email do usuário;
     * @param password Senha do usuário;
     * @param model Container de dados para renderização de páginas;
     * @return Nome da página para renderização.
     */
    @PostMapping("/register-process")
    public String processRegistration(@RequestParam String login,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      Model model) {


        // Verificar se o usuário existe
        if(userRepository.findByLogin(login) != null) {
            model.addAttribute("errorMessage", "Login já existe");
            return "login/register";
        }

        // Criptografar senha e criar usuário com papel USER
        String encryptedPassword = passwordEncoder.encode(password);
        User newUser = new User(login, email, encryptedPassword, UserRole.USER);

        // Salvar usuário
        userRepository.save(newUser);

        // Redirecionar para login com mensagem de sucesso
        model.addAttribute("successMessage", "Cadastro realizado com sucesso");
        return "login/login";
    }
}
