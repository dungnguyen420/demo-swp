package com.example.swp.Config;

import com.example.swp.Service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/index","/package","/home","/ws/**","/auth/**","/orders/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers(
                                "/home",
                                "/trainers/detail/**",
                                "/classes",
                                "/style.css",
                                "/package",
                                "/trainers",
                                "/classes",
                                "/classes/**",
                                "/cart/view",
                                "/shop",
                                "/shop/add",
                                "/cart/add",
                                "/cart/update",
                                "/cart/clear",
                                "/orders/create",
                                "/orders/**",
                                "/managers/**"
                        ).permitAll()
                        .requestMatchers("/cart/**").authenticated()
                        .requestMatchers("/chat").authenticated()
                        .requestMatchers("/admin/**",
                                "/trainers/create",
                                "/trainers/edit/**",
                                "/trainers/delete/**").hasRole("ADMIN")
                        .requestMatchers("/products/**", "/equipment/**").hasRole("MANAGER")
                        .requestMatchers("/ws/**","/admin/chat/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/index")
                        .loginProcessingUrl("/index")
                        .usernameParameter("usernameOrEmail")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


}
