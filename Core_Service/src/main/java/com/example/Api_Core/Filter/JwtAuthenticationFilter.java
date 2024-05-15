package com.example.Api_Core.Filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

// Classe de filtre pour l'authentification JWT
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    // Méthode pour effectuer le filtrage de la requête
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Récupérer le token JWT de l'en-tête Authorization
            final String authHeader = request.getHeader("Authorization");
            logger.info("Received Authorization Header: " + authHeader);

            // Vérifier la présence et le format correct du token JWT
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String jwt = authHeader.substring(7);
                final String identifier = jwtService.extractUsername(jwt);

                // Vérifier si l'utilisateur est authentifié
                if (identifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Vérifier si le token JWT est valide
                    if (jwtService.isTokenValid(jwt)) {
                        logger.info("Authentication successful for user: " + identifier);
                        // Créer une authentification sans détails de l'utilisateur
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                identifier,
                                null,
                                null
                        );

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // Définir l'authentification dans le contexte de sécurité
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        logger.warning("Token is not valid for user: " + identifier);
                        SecurityContextHolder.clearContext();
                    }
                }
            }
        } catch (Exception e) {
            // Gérer les exceptions pendant l'authentification
            logger.warning("Exception during authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

