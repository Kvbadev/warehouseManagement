package com.kvbadev.wms.presentation.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.presentation.dataTransferObjects.JwtDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final String jwtAudience;
    private final String jwtIssuer;
    private final String jwtSecret;
    private final String jwtType;
    private final int jwtExpiration;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            String jwtAudience, String jwtIssuer,
            String jwtSecret, String jwtType, int jwtExpiration
    ) {
        this.jwtAudience = jwtAudience;
        this.jwtIssuer = jwtIssuer;
        this.jwtSecret = jwtSecret;
        this.jwtType = jwtType;
        this.jwtExpiration = jwtExpiration;
        this.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authentication
    ) throws EntityNotFoundException, IOException {
        User user = (User) authentication.getPrincipal();
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        String token = Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS512)
                .issuer(jwtIssuer)
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .audience().add(jwtAudience)
                .and()
                .header().add("typ", jwtType)
                .and()
                .compact();

        //TODO make it cleaner
        JwtDto responseToken = new JwtDto(token);
        response.setContentType("application/json;charset=UTF-8");
        response.getOutputStream().write(objectMapper.writeValueAsBytes(responseToken));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        throw failed;
    }
}
