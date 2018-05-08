package org.alex323glo.its_simulator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Web MVC resources' configuration class
 * (customizes ResourceHandlers of Spring MVC module).
 *
 * @author Alexey_O
 * @version 0.1
 */
@Configuration
@EnableWebMvc
public class WebMvcResourcesHandlerConfiguration implements WebMvcConfigurer {

    private final Environment environment;

    @Autowired
    public WebMvcResourcesHandlerConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/",
                environment.getProperty("front-part.index-page.url"));
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
