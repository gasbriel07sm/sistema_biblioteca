package com.unidevs.core_system.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ViewController para navegação Web
 *
 * Este controlador é a central de navegação que gerencia o roteamento de todas as páginas do sistema.
 *
 * O controlador é responsável por:
 * - Gerenciar redirecionamentos entre páginas do sistema;
 * - Renderizar os templates corretos para cada rota;
 * - Centralizar a lógica de navegação em um único lugar.
 *
 * Componentes:
 * - SecurityConfigurations: define quais rotas deste controlador são públicas ou privadas;
 * - AuthenticationViewController: complementa este controlador, processando os formulários de login/registro;
 * - SecurityFilter: valida os Token JWT antes das requisições chegarem a este controlador;
 * - Spring DispatcherServlet: encaminha as requisiçoes HTTP para os métodos apropriados;
 * - Thymeleaf/templates: os retornos string representam as páginas que serão renderizadas.
 */

@Controller
public class ViewController {

    /** homePage() Mapeia a rota raíz do sistema.
     * Redireciona automaticamente para a página de login.
     *
     * @return String da página de login.
     */
    @GetMapping("/")
    public String homePage(){
        return "redirect:/login";
    }

    /** loginPage() Mapeia a rota de login.
     * Configurada como pública pelo SecurityConfigurations.
     *
     * @return Template da página de login.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login/login";  // Updated path
    }

    /** registerPage() Mapeia a rota de registro.
     * Configurada como pública pelo SecurityConfigurations.
     *
     * @return Template da página de registro.
     */
    @GetMapping("/register")
    public String registerPage() {
        return "login/register";  // Updated path
    }


    /** home() Mapeia a rota home.
     * Protegida pelo SecurityConfigurations.
     *
     * @return Template da página inicial.
     */
    @GetMapping("/home")
    public String home() {
        return "index"; // This renders your index.html template
    }
}
