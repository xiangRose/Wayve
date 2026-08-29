package com.Grassroot.JobSearch.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.cors-origins}")
    private String corsOrigins;

    @Value("${app.front-dir:front}")
    private String frontDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(corsOrigins.split(","))
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path front = Paths.get(frontDir).toAbsolutePath().normalize();
        String location = "file:" + front + "/";
        registry.addResourceHandler("/css/**").addResourceLocations(location + "css/");
        registry.addResourceHandler("/js/**").addResourceLocations(location + "js/");
        registry.addResourceHandler("/assets/**").addResourceLocations(location + "assets/");
        registry.addResourceHandler("/index.html").addResourceLocations(location);
        registry.addResourceHandler("/**").addResourceLocations(location).resourceChain(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
