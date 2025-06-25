/** NgModule } : on importe le décorateur NgModule qui permet de transformer une classe normale en module Angular.
@angular/core : c’est la bibliothèque principale d’Angular. Elle contient tous les éléments de base pour créer une application Angular. */
import { NgModule } from '@angular/core';
/**Élément	      Rôle
RouterModule	    Active le système de navigation Angular
Routes	          Type qui permet de créer une liste de chemins (URLs) et leurs composants */
import { RouterModule, Routes } from '@angular/router';
//ProduitListComponent	Composant affiché quand on va sur /categorie/:id
import { ProduitListComponent } from './components/produit-list/produit-list.component';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { ProduitDetailComponent } from './components/produit-detail/produit-detail.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileEditComponent } from './components/profile-edit/profile-edit.component';
import { RgpdComponent } from './components/rgpd/rgpd.component';
import { CgvComponent } from './components/cgv/cgv.component';
import { ContactComponent } from './components/contact/contact.component';
import {AddProductComponent} from './components/add-product/add-product.component';
import {EditProductComponent} from './components/edit-product/edit-product.component';
import { PanierComponent } from './components/panier/panier.component';
/**
 *C’est comme si tu créais une carte GPS pour Angular.
Chaque ligne dans ce tableau dit :
“Si quelqu’un tape telle adresse (URL), montre-lui tel bâtiment (composant)
 */
//Toujours mettre les routes les plus spécifiques en haut de la liste, car elles sont plus précises.
const routes: Routes = [
  {path:"produit/:id", component: ProduitDetailComponent}, // ✅ Détail produit (US03)
  {path: "categorie/:id", component: ProduitListComponent},
  {path:"categories", component: CategoryListComponent},
  {path:"", redirectTo: "/categories", pathMatch: "full"},
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile-edit/:id', component: ProfileEditComponent },
  { path: 'rgpd', component: RgpdComponent },
  { path: 'conditions-generales', component: CgvComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'add-product', component: AddProductComponent },
  { path: 'edit-product/:id', component: EditProductComponent },
  { path: 'panier', component: PanierComponent }



  
];

/**C’est un décorateur Angular. Il permet de dire à Angular :
“Ce fichier est un module. Voici ce qu’il utilise et ce qu’il partage. */
@NgModule({
  /**On importe le système de routage d’Angular ici. 
RouterModule.forRoot(routes) signifie :
“Je veux initialiser le système de routing avec les routes que j’ai définies juste au-dessus.”
forRoot(...) est utilisé une seule fois dans toute l'application (dans le module principal). */
  imports: [RouterModule.forRoot(routes)],

  /**Cela veut dire :
“Je rends le système de routage accessible aux autres parties de l'application.” */
  exports: [RouterModule]
})
export class AppRoutingModule { }
/**On exporte la classe pour qu’elle puisse être importée ailleurs (notamment dans AppModule).
C’est comme dire : “Je termine le module de routing, il est prêt à être utilisé.” */


/**
 Imagine que ton app est une maison.
 Chaque pièce (page ou composant) est un endroit différent.
 Le module de routing, c’est comme le plan de la maison : il indique comment aller d’une pièce à l’autre.
 Sans ce plan (le module), personne ne sait comment se déplacer dans la maison.
 * */

 /** Qu’est-ce que @angular/router ?
C’est le module officiel d’Angular pour gérer les routes.

Il contient tout ce qu’il faut pour :

Créer une navigation entre pages (comme /produits, /categorie/1)
Passer des paramètres dans l’URL (ex. :id)
Gérer les redirections
Afficher dynamiquement les composants dans la page

3. Qu’est-ce que RouterModule ?
C’est le module principal du système de navigation.

Il permet d’activer le routing dans ton application.
C’est lui que tu vas configurer avec la liste des routes dans forRoot() (tu verras ça dans quelques lignes).

🧠 Tu peux voir RouterModule comme le GPS de ton application Angular. Il sait quelle URL mène à quel composant.
 Qu’est-ce que Routes ?
C’est un type Angular (défini avec TypeScript).
Il sert à déclarer la liste des routes disponibles dans l’app. */