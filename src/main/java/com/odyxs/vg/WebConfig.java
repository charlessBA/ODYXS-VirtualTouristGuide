package com.odyxs.vg;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.file.Paths;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("es"));
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta del proyecto en disco (funciona en dev y en prod)
        String staticPath = "file:" + Paths.get(System.getProperty("user.dir"),
                "src", "main", "resources", "static").toAbsolutePath() + "/";

        // Imágenes subidas por el admin
        String uploadPath = "file:" + Paths.get(System.getProperty("user.dir"),
                "src", "main", "resources", "static", "uploads").toAbsolutePath() + "/";

        // /img/** → busca primero en disco, luego en classpath (jar)
        registry.addResourceHandler("/img/**")
                .addResourceLocations(staticPath + "img/", "classpath:/static/img/");

        // /uploads/** → solo en disco
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);

        // CSS, JS y demás estáticos
        registry.addResourceHandler("/css/**")
                .addResourceLocations(staticPath + "css/", "classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations(staticPath + "js/", "classpath:/static/js/");
    }
}
