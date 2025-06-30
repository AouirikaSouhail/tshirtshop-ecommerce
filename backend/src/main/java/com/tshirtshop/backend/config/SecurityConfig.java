/**ce fichier SecurityConfig.java est une configuration de base qui enregistre un encodeur de mot de passe
 *  avec Spring. Voici une explication détaillée ligne par ligne :*/

// Déclare le package dans lequel se trouve la classe SecurityConfig
package com.tshirtshop.backend.config;

/** Ces imports permettent d'utiliser :
 @Configuration → pour déclarer une classe de configuration Spring.
 @Bean → pour enregistrer manuellement un composant (ici, le password encoder).
 BCryptPasswordEncoder → la classe de Spring Security qui chiffre les mots de passe avec l’algorithme BCrypt.*/
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// Utilitaires
import java.util.Arrays;

// JWT filter
import com.tshirtshop.backend.security.JwtAuthenticationFilter;
import org.springframework.http.HttpMethod;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
//Cette annotation marque la classe comme une source de configuration Spring. Elle sera analysée au démarrage.
@Configuration // Dit à Spring : "Ceci est une classe de configuration"
//Déclaration de la classe de configuration.
// Cette classe configure la sécurité Spring, y compris l’authentification, les droits d’accès, et le support CORS.
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    //Cette annotation dit à Spring de créer une instance de retour de la méthode suivante (ici, un BCryptPasswordEncoder)
    // et de la rendre disponible partout dans l’application (via injection).
    @Bean // Dit à Spring : "Crée et gère cet objet"
    /**Cette méthode crée un encodeur BCrypt utilisé pour :
     Hacher un mot de passe avant de le sauvegarder.
     Comparer un mot de passe brut avec un mot de passe hashé stocké en base.*/
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Je crée une chaîne de sécurité (un ensemble de règles) pour protéger mon site web.
    /**
     * Ce code :
     * Autorise Angular à parler à ton backend (CORS).
     * Désactive une vieille protection inutile (CSRF).
     * Laisse tout le monde accéder à certaines routes publiques.
     * Bloque tout le reste si on n’est pas connecté.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Tu crées ici la logique principale de sécurité : qui peut accéder à quoi ?
        http.cors(Customizer.withDefaults()) // Active la configuration CORS (voir méthode suivante)
                .csrf(csrf -> csrf.disable())    // Désactive la protection CSRF (inutile pour API REST sans session)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/stripe/webhook").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/stripe/create-checkout-session").permitAll() // 🆕 Paiement Stripe
                        .requestMatchers(HttpMethod.POST, "/api/test-mail").permitAll() // 🆕 test d’envoi d’e-mail
                        .requestMatchers(HttpMethod.GET,  "/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/products/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/products/{id}").hasRole("ADMIN")       // 🛡️ Accès réservé aux admins
                        .requestMatchers(HttpMethod.DELETE, "/products/{id}").hasRole("ADMIN")    // 🛡️ Accès réservé aux admins
                        .requestMatchers(HttpMethod.POST, "/products/category/{categoryId}").hasRole("ADMIN") // 🛡️
                        .anyRequest().authenticated() // 🔒 Tout le reste doit être authentifié
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // ⬅️ Ajout du filtre ici

        return http.build();
    }

    /** AuthenticationManager C’est le chef d’orchestre de l’authentification chez Spring.
     * Il vérifie si l’e-mail et le mot de passe sont valides.
     @Bean	On le déclare comme composant utilisable partout dans l’application.
     AuthenticationConfiguration	C’est un objet que Spring fournit automatiquement.
     Il sait comment créer un AuthenticationManager en fonction de ta config.*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Filtre CORS pour permettre les appels entre Angular et Spring
    // Méthode qui crée un filtre CORS pour autoriser les échanges entre le frontend Angular et le backend Spring Boot
    @Bean
    public CorsFilter corsFilter() {
        // Création d'un objet de configuration CORS
        CorsConfiguration config = new CorsConfiguration();
        // Autorise l'envoi de cookies ou d'en-têtes d'authentification dans les requêtes (ex : token JWT)
        config.setAllowCredentials(true);
        // Déclare les origines autorisées à appeler ce backend
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // frontend Angular
        // Définit les en-têtes HTTP autorisés dans les requêtes envoyées au serveur
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        // Spécifie les méthodes HTTP autorisées : GET, POST, PUT, DELETE, OPTIONS
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setExposedHeaders(Arrays.asList("Authorization")); // ⬅️ pour que le frontend voie le token
        // Enregistre cette configuration pour toutes les routes de l'application (/**)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // Retourne un nouveau filtre CORS basé sur cette configuration
        return new CorsFilter(source);
    }
}

/** Ce filtre est indispensable si ton frontend (Angular) tourne sur un port (4200) différent de ton backend (8080),
 *  car le navigateur bloque sinon les requêtes dites cross-origin. Ce code
 * lève cette barrière de sécurité en toute maîtrise, pour permettre les appels entre tes deux applications en développement local.
 */
