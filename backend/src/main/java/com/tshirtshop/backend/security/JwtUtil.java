// 🔐 Paquet de sécurité
package com.tshirtshop.backend.security;

// 📦 Import des classes nécessaires de la bibliothèque jjwt (Java JWT)
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 *  Cette classe est un outil pour manipuler les tokens JWT.
 * Elle permet :
 * - de générer un token pour un utilisateur,
 * - de lire les informations du token (ex: email),
 * - de vérifier si un token est toujours valide.
 */
@Component // Permet à Spring de gérer cette classe automatiquement (bean injectable)
public class JwtUtil {

    //  Clé secrète pour signer et vérifier les tokens. Elle est générée automatiquement ici.
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Utilise l'algorithme HMAC-SHA256

    // Durée de validité d’un token : ici, 10 heures (en millisecondes)
    private final long expirationTime = 10 * 60 * 60 * 1000;

    /**
     *  Méthode pour créer un token JWT en donnant un email.
     */
    public String generateToken(String email) {
        return Jwts.builder() //  Commence la construction du token
                .setSubject(email) //  Le "sujet" du token (souvent l'identifiant) → ici, l’email de l’utilisateur
                .setIssuedAt(new Date()) //  Date de création du token
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Date d’expiration
                .signWith(secretKey) // Signature avec la clé secrète
                .compact(); // Finalise la construction et retourne le token sous forme de String
    }

    /**
     * 📤 Extrait l’email (sujet) à partir d’un token donné
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject); // récupère ce qu’on a mis en setSubject → l’email
    }

    /**
     * Vérifie si un token est encore valide pour un email donné
     */
    public boolean isTokenValid(String token, String userEmail) {
        String email = extractEmail(token); // extrait l’email contenu dans le token
        return (email.equals(userEmail) && !isTokenExpired(token)); //  Compare et vérifie l’expiration
    }

    /**
     *  Vérifie si le token a expiré
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration); // récupère la date d’expiration
        return expiration.before(new Date()); // Compare à la date actuelle
    }

    /**
     * Méthode générique pour extraire un champ ("claim") spécifique du token (email, date, etc.)
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder() // Prépare le déchiffrage du token
                .setSigningKey(secretKey) //  Clé utilisée pour vérifier la signature
                .build() // Construit le parser
                .parseClaimsJws(token) // Analyse le token signé
                .getBody(); // Récupère les données à l’intérieur du token
        return claimsResolver.apply(claims); // Récupère le champ demandé (email, date, etc.)
    }
}
