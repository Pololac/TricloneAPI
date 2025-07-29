package com.hb.cda.examapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain accessControl (HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    // Authentification requise pour /api/accounts et tout ce qui commence par /api/accounts/
                    .requestMatchers("/api/accounts", "/api/accounts/**").authenticated()
                    // Tout le reste reste public
                    .anyRequest().permitAll()
            )
            // Ici tu peux choisir IF_REQUIRED ou STATELESS selon ton besoin
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            // Méthode d’authentification "Basic" : pas via /api/login mais en rajoutant
            // un header "Authorization" -> "Basic Auth" (username+MdP) dans les requêtes Postman
            .httpBasic(Customizer.withDefaults()

            /* Si on passe par un formulaire, mais pas recommandé sur API Rest
            .formLogin(form -> form
                    .loginProcessingUrl("/api/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler((req,res,auth)->res.setStatus(HttpServletResponse.SC_OK))
                    .failureHandler((req,res,e)->res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
             */
            );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

