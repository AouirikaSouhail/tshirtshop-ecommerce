package com.tshirtshop.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.tshirtshop.backend.model.User;
import com.tshirtshop.backend.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String userEmail = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);

            try {
                userEmail = jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                // 🔒 Token invalide ou mal formé → ne bloque pas la requête
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(userEmail).orElse(null);

            if (user != null && user.getRole() != null && !user.getRole().isEmpty()
                    && jwtUtil.isTokenValid(jwt, user.getEmail())) {

                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(user.getRole()));

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // ✅ Exclure les routes publiques (login et register) du filtre
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/api/login") || path.equals("/api/register") || path.equals("/api/test-mail");
    }
}
