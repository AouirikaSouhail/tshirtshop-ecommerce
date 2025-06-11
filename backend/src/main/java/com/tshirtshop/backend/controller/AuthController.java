/**Gérer la connexion d’un utilisateur. C’est-à-dire : recevoir un email + mot de passe, chercher l’utilisateur
 * en base de données, vérifier que le mot de passe est correct,puis répondre.
 */
package com.tshirtshop.backend.controller;

import com.tshirtshop.backend.dto.LoginRequest; // Pour récupérer l’email et le mot de passe envoyés par le frontend.
import com.tshirtshop.backend.dto.RegisterRequest; // pour l inscription et vérification email unique.
import com.tshirtshop.backend.model.User; //  Pour manipuler les utilisateurs.
import com.tshirtshop.backend.repository.UserRepository; //  pour interagir avec la base de données.
import org.springframework.http.ResponseEntity; // pour envoyer des réponses personnalisées (200 OK, 400 BAD REQUEST, etc.).
import org.springframework.web.bind.annotation.*;// @RestController, @RequestMapping, @PostMapping, @RequestBody →
                                                 // des annotations Spring pour gérer les routes HTTP.
import java.util.Optional;
@RestController // "Ceci est un contrôleur REST."Cela veut dire que cette classe va répondre aux requêtes HTTP (POST, GET, etc.) avec des objets JSON.
@RequestMapping("/api")// Toutes les routes de cette classe commencent par /api. Donc ici, la route complète sera /api/login.
public class AuthController{

    private final UserRepository userRepository; // Tu déclares une variable de type UserRepository, que tu vas utiliser pour accéder à la base de données des utilisateurs.

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login") // Cette méthode va répondre à une requête POST vers /api/login.
    /**
     * Cette méthode gère la connexion des utilisateurs via la route POST /api/login.
     * Elle reçoit un objet JSON contenant l'email et le mot de passe de l'utilisateur.
     * Grâce à l'annotation @RequestBody, ces données sont automatiquement converties
     * en un objet LoginRequest (DTO) contenant les champs email et password.
     *
     * Étapes :
     * 1. Recherche de l'utilisateur en base de données via son adresse email.
     * 2. Vérifie si l'utilisateur existe, sinon renvoie une réponse 400 avec message d'erreur.
     * 3. Vérifie si le mot de passe correspond à celui stocké en base (en clair ici).
     * 4. Si les identifiants sont corrects, renvoie une réponse HTTP 200 (OK) avec les données de l'utilisateur.
     *
     * @param loginRequest Objet contenant l'email et le mot de passe fournis par l'utilisateur.
     * @return ResponseEntity contenant soit l'objet utilisateur (connexion réussie),
     *         soit un message d'erreur (email non trouvé ou mot de passe incorrect).
     */
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
        }
        //On récupère l'utilisateur à partir de l’objet Optional.
        // On ne fait ça que parce qu’on est sûr que optionalUser n’est pas vide (grâce à if juste au-dessus).
        User user = optionalUser.get();
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.badRequest().body("Mot de passe incorrete.");
        }
            return ResponseEntity.ok().body(user); // Pour l'instant, on renvoie l’objet User directement.

            //Si tout est bon → on renvoie l’objet User au frontend avec une réponse 200 (OK).
            // Attention, renvoyer l’objet User tel quel expose aussi le mot de passe.
            // créer un DTO de réponse (UserResponse) pour cacher les données sensibles.
        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
          // On vérifie si un utilisateur existe déjà avec cet email
          Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
          if (existingUser.isPresent()) {
              return ResponseEntity.badRequest().body("Email déja utilisé.");
          }
          User newUser = new User();
          newUser.setName(registerRequest.getFullName());
          newUser.setEmail(registerRequest.getEmail());
          newUser.setPassword(registerRequest.getPassword());

          userRepository.save(newUser);
          return ResponseEntity.ok().body("Inscription réussie.");
        }
    }


/*“Le contrôleur AuthController gère la route /api/login. Il reçoit un LoginRequest, cherche l’utilisateur par email,
 vérifie le mot de passe, et renvoie une réponse adaptée. Pour l’instant, les mots de passe sont comparés directement,
 mais on prévoira de les chiffrer avec BCrypt. Le code utilise Optional pour éviter les erreurs null et
 ResponseEntity pour renvoyer des statuts HTTP clairs.” */
