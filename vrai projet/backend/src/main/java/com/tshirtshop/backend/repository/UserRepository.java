/**Créer une interface qui te permet de manipuler automatiquement les données de l'entité User
 * (inscription, recherche par email, etc.) sans écrire de SQL.
*/
package com.tshirtshop.backend.repository;

import com.tshirtshop.backend.model.User;

/** Tu importes ici l'interface magique de Spring Data JPA. Elle contient déjà toutes les méthodes de base comme :
 save() → enregistrer en base
 findAll() → retrouver tous les utilisateurs
 findById() → retrouver un utilisateur via son id
 deleteById() → supprimer un utilisateur
  Grâce à ça, pas besoin d’écrire du SQL à la main !
 */

import org.springframework.data.jpa.repository.JpaRepository;

//Un Optional<T> est une boîte qui peut contenir une valeur ou rien.
//Ça évite les erreurs NullPointerException.
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
