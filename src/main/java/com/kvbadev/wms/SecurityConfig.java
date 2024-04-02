package com.kvbadev.wms;

import com.kvbadev.wms.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> {
                            a.requestMatchers(HttpMethod.POST, "/users**").hasRole("ADMIN");
                            a.requestMatchers(HttpMethod.PUT, "/users**").hasRole("ADMIN");
                            a.requestMatchers(HttpMethod.PATCH, "/users/*").hasRole("ADMIN");
                            a.requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN");
                            a.requestMatchers(HttpMethod.GET, "/users**").hasRole("STAFF");
                            a.requestMatchers(HttpMethod.GET, "/users/*").hasRole("STAFF");
                            a.requestMatchers("/items**", "/parcels**", "/deliveries**").hasRole("USER");
                            a.anyRequest().authenticated();
                        }
                )
                .httpBasic(Customizer.withDefaults())
                .cors(c -> {
                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(List.of("*"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);

                    c.configurationSource(source);
                })
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
