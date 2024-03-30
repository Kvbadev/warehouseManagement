package com.kvbadev.wms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
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
                .cors(Customizer.withDefaults())
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
