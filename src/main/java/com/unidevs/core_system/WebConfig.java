package com.unidevs.core_system;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração Web da aplicação.
 * Implementa a interface {@link WebMvcConfigurer} para customizar o comportamento do Spring MVC em relação
 * ao tratamento de recursos estáticos.
 *
 * Permite o acesso a arquivos armazenados localmente (como imagens enviadas pelo usuário) através de URLs públicas.
 *
 * @WebConfig
 * Configura o mapeamento de recursos estáticos, permitindo o acesso via URL os arquivos salvos no diretório "uploads"
 * Assim, um arquivo armazenado como uploads/capa.jpg poderá ser acessado publicamente via:
 * http://localhost:8080/uploads/capa.jpg
 *
 * Parâmetros:
 * @param registry Registro de manipuladores de recursos, usado para adicionar novas localizações estáticas.
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}