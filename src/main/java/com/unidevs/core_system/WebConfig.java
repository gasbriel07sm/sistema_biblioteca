package com.unidevs.core_system;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* @Configuration: é uma fonte de configurações para a aplicação
 * Executa os métodos para customizar o comportamento padrão
 * Detecta esta classe durante a inicialização e aplicar as configurações definidas
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /* 'implements WebMvcConfigurer': oferece um conjunto de métodos "vazios" que podemos sobrescrever (@Override) para customizar diferentes partes do framework Spring Web MVC, como formatadores, interceptadores, e, neste caso, manipuladores de recursos estáticos.
     */

    /* @Override: indica que estamos sobrescrevendo um metodo da interface WebMvcConfigurer.
       public void addResourceHandlers(ResourceHandlerRegistry registry): nos dá um "registro" onde podemos adicionar nossos próprios manipuladores de recursos (resource handlers).
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. registry.addResourceHandler("/uploads/**"):
        //    Define o padrão da URL que será interceptado por esta regra.
        //    "/uploads/**" significa "qualquer requisição que comece com /uploads/".
        //    O duplo asterisco (**) é um curinga que corresponde a qualquer caminho recursivamente.
        registry.addResourceHandler("/uploads/**")
                // 2. .addResourceLocations("file:uploads/"):
                //    Define o local FÍSICO no sistema de arquivos onde o Spring deve procurar os arquivos correspondentes ao padrão da URL.
                //    "file:": procurar em um caminho  no sistema de arquivos do servidor, em vez de procurar dentro do classpath do projeto (dentro do arquivo .jar).
                //    "uploads/": procurar por uma pasta chamada 'uploads' no mesmo diretório onde a aplicação foi iniciada.
                .addResourceLocations("file:uploads/");
    }
}