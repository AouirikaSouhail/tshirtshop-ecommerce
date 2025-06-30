//  Indique où se trouve la classe dans ton projet (obligatoire en Java)
package com.tshirtshop.backend.repository;

/** On importe :

Ta classe Category

L’interface JpaRepository fournie par Spring pour accéder à la base MySQL automatiquement

**/

import com.tshirtshop.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long>{

/**
 *C ’est ici que la magie opère :
 *
 * Tu dis à Spring : “Je veux une interface pour gérer les Category”
 *
 * JpaRepository<Category, Long> signifie :
 *
 * Tu veux manipuler des objets Category
 *
 * Leur identifiant est un Long (comme id)
 *
 * 🟢 Tu auras automatiquement les méthodes :
 *
 * findAll() pour lister les catégories
 *
 * save() pour en ajouter une
 *
 * deleteById() pour supprimer
 *
 * etc.
 */
}


