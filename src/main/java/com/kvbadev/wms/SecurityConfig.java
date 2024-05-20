package com.kvbadev.wms;

import com.kvbadev.wms.presentation.filters.JwtAuthenticationFilter;
import com.kvbadev.wms.presentation.filters.JwtAuthorizationFilter;
import com.kvbadev.wms.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.type}")
    private String jwtType;
    @Value("${jwt.audience}")
    private String jwtAudience;
    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }
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
                .authorizeHttpRequests(a -> a
                            .requestMatchers(HttpMethod.POST, "/users**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/users**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/users/*").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/users**").hasRole("STAFF")
                            .requestMatchers(HttpMethod.GET, "/users/*").hasRole("STAFF")
                            .requestMatchers("/items**", "/parcels**", "/deliveries**").hasRole("USER")
                            .requestMatchers("/**").permitAll()
//                            .anyRequest().authenticated()
                )
                .cors(c -> {
                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(List.of("*"));
                    configuration.setAllowedMethods(List.of("*"));
                    configuration.setAllowedHeaders(List.of("*"));

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);

                    c.configurationSource(source);
                })
//                .httpBasic(Customizer.withDefaults())
                .addFilter(new JwtAuthenticationFilter(
                        authenticationManager(), jwtAudience, jwtIssuer, jwtSecret, jwtType, jwtExpiration
                ))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService(), jwtSecret))
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
