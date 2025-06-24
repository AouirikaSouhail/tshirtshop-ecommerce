/**Gérer la connexion d’un utilisateur. C’est-à-dire : recevoir un email + mot de passe, chercher l’utilisateur
 * en base de données, vérifier que le mot de passe est correct,puis répondre.
 */
package com.tshirtshop.backend.controller;

import com.tshirtshop.backend.dto.LoginRequest; // Pour récupérer l’email et le mot de passe envoyés par le frontend.
import com.tshirtshop.backend.dto.RegisterRequest; // pour l inscription et vérification email unique.
import com.tshirtshop.backend.dto.UpdateUserRequest;
import com.tshirtshop.backend.model.User; //  Pour manipuler les utilisateurs.
import com.tshirtshop.backend.repository.UserRepository; //  pour interagir avec la base de données.
import com.tshirtshop.backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // pour envoyer des réponses personnalisées (200 OK, 400 BAD REQUEST, etc.).
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;// @RestController, @RequestMapping, @PostMapping, @RequestBody →
                                                 // des annotations Spring pour gérer les routes HTTP.


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.tshirtshop.backend.security.JwtUtil;

@RestController // "Ceci est un contrôleur REST."Cela veut dire que cette classe va répondre aux requêtes HTTP (POST, GET, etc.) avec des objets JSON.
@RequestMapping("/api")// Toutes les routes de cette classe commencent par /api. Donc ici, la route complète sera /api/login.
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController{

    private final UserRepository userRepository; // Tu déclares une variable de type UserRepository, que tu vas utiliser pour accéder à la base de données des utilisateurs.
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    // Modifie le constructeur pour inclure le passwordEncoder
    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil,AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
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
        // Étape 1 : Récupérer l'utilisateur depuis la base
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
        }

        // On récupère l'utilisateur à partir de l’objet Optional.
        // On ne fait ça que parce qu’on est sûr que optionalUser n’est pas vide (grâce à if juste au-dessus).
        User user = optionalUser.get();

        // Modifier la méthode login() pour vérifier un mot de passe haché
        /** loginRequest.getPassword() → c’est le mot de passe en clair tapé par l'utilisateur.
         user.getPassword() → c’est le mot de passe haché en base (ex : $2a$10$yKy...).
         passwordEncoder.matches() → c’est une méthode qui compare intelligemment les deux et renvoie true ou false. */
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Mot de passe incorrect.");
        }

        // ✅ Génération du token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // ✅ Construction de la réponse avec plusieurs champs
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());

        // Si tout est bon → on renvoie l’objet User au frontend avec une réponse 200 (OK).
        // Attention, renvoyer l’objet User tel quel expose aussi le mot de passe.
        // créer un DTO de réponse (UserResponse) pour cacher les données sensibles.

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
          // On vérifie si un utilisateur existe déjà avec cet email
          Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
          if (existingUser.isPresent()) {
              return ResponseEntity.badRequest().body("Email déja utilisé.");
          }
          User newUser = new User();
          newUser.setName(registerRequest.getName());
          newUser.setEmail(registerRequest.getEmail());
          // Cela hachera le mot de passe avant de le sauvegarder en base. Rajout méthode passwordEncoder.encode()
          newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

          userRepository.save(newUser);
          return ResponseEntity.ok().body(Collections.singletonMap("message", "Inscription réussie."));

          /** Que fait Collections.singletonMap("message", "Inscription réussie.") exactement ?
           Cette méthode ne parse rien du tout. Elle :

            Crée un objet Java de type Map (clé/valeur)
           Avec une seule paire :

           clé : "message"

           valeur : "Inscription réussie." */
        }

        //Tu veux mettre à jour un utilisateur existant (par exemple, changer son email ou mot de passe) → on utilise PUT
        //La partie {id} dans l’URL, c’est une variable dynamique.
        //Elle permet de dire au backend : « Je veux modifier l’utilisateur numéro 12 »
        // Exemple : Si tu fais une requête : PUT /api/user/12

        @PutMapping("/user/{id}")
        //@PathVariable Long id signifie : « Prends le numéro dans l’URL et mets-le dans la variable id ».
        public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        //On cherche en base si un utilisateur avec cet ID existe. Le résultat est un Optional<User>, qui peut être vide ou non.
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur introuvable"));// Si aucun utilisateur avec cet ID n’existe, on renvoie une réponse 404 Not Found.
        }

        //On récupère l'utilisateur à partir de l'Optional. Ici on est sûr que l’utilisateur existe (grâce au if juste au-dessus)
        User user  = optionalUser.get();

        // vérifier si l'email est utilisé par quelqu'un d'autre

            Optional<User> userWithEmail = userRepository.findByEmail(updateUserRequest.getEmail());
           //Un utilisateur existe déjà avec cet e-mail ?" &&
            // && Si oui, alors on vérifie si cet utilisateur, ce n’est pas le même que celui qu’on est en train de modifier.
           // Cela veut dire : "Ce n’est pas le même utilisateur (donc quelqu’un d’autre utilise déjà cet e-mail)".
            if (userWithEmail.isPresent() && ! userWithEmail.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email déjà utilisé par un autre utilisateur."));

            }
            // Mise à jour après vérification
            user.setName(updateUserRequest.getName());
            user.setEmail(updateUserRequest.getEmail());
            user.setPassword(updateUserRequest.getPassword());
            userRepository.save(user);

            return ResponseEntity.ok(Collections.singletonMap("message", "Profil mis à jour avec succès !"));

        }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur non trouvé"));
        }

        return ResponseEntity.ok(optionalUser.get());
    }
    }


/*“Le contrôleur AuthController gère la route /api/login. Il reçoit un LoginRequest, cherche l’utilisateur par email,
 vérifie le mot de passe, et renvoie une réponse adaptée. Pour l’instant, les mots de passe sont comparés directement,
 mais on prévoira de les chiffrer avec BCrypt. Le code utilise Optional pour éviter les erreurs null et
 ResponseEntity pour renvoyer des statuts HTTP clairs.” */
