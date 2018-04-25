package org.alex323glo.its_simulator.security;

import org.alex323glo.its_simulator.security.handlers.CustomAuthenticationFailureHandler;
import org.alex323glo.its_simulator.security.handlers.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main security configuration class
 * (customizes logic of Spring Security module).
 *
 * @author Alexey_O
 * @version 0.1
 */
@Configuration
@EnableWebSecurity
public class MainSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // TODO replace this method's logic with real UserDetailsService referencing!
    @Override
    protected UserDetailsService userDetailsService() {
        return username -> User.builder()
                .username("alex323glo")
                .password(passwordEncoder().encode("12345678"))
                .roles("USER")
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/private/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
//                    .logoutSuccessHandler(logoutSuccessHandler())
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /*
        Handlers:
     */

    @Bean
    protected CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    protected CustomAuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

//    @Bean
//    protected LogoutSuccessHandler logoutSuccessHandler() {
////        return (request, response, authentication) -> {
////            if (authentication.isAuthenticated()) {
////                try {
////                    request.logout();
////                } catch (ServletException e) {
////                    try {
////                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "");
////                    } catch (IOException e1) {
////                        e1.printStackTrace();
////                    }
////                }
////            }
////        };
//        return (request, response, authentication) -> {
//            try {
//
//            } catch (IOException ioe) {
//
//            }
//        };
//    }
}
