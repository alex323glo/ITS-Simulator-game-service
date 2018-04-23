package org.alex323glo.its_simulator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration
 */
@Configuration
@EnableWebMvc
public class CustomWebMvcResourcesConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Autowired
    public CustomWebMvcResourcesConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/public/",
                        "classpath:/static/",
                        environment.getProperty("front-part.location.absolute"));
    }
}
