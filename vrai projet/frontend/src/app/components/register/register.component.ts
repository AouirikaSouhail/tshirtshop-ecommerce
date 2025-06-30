import { Component } from '@angular/core';              // pour déclarer ton composant Angular
import { HttpClient } from '@angular/common/http';      // pour faire des appels HTTP (POST ici)
import { Router } from '@angular/router';               // pour rediriger l'utilisateur après l'inscription (vers /login)

/**Ceci est un décorateur Angular : Il dit à Angular :
 * selector   → ce composant sera utilisé avec <app-register></app-register>
 * templateUrl → fichier HTML associé à ce composant
 * styleUrls  → fichier CSS associé */
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

// Tu déclares ici la classe principale du composant.
export class RegisterComponent {

  /**Ces propriétés sont liées aux champs du formulaire
   * errorMessage et successMessage sont utilisées pour afficher des messages
   * au-dessus ou en dessous du formulaire */

  firstName  : string = '';
  lastName   : string = '';
  adresse    : string = '';
  codePostal : string = '';
  ville      : string = '';
  email      : string = '';
  password   : string = '';

  errorMessage   : string = '';
  successMessage : string = '';

  /**Le constructeur injecte les dépendances :
   *  HttpClient pour faire un appel .post()
   *  Router     pour rediriger avec .navigate() */
  constructor(private http: HttpClient, private router: Router) {}

  // C’est une méthode déclenchée quand on clique sur le bouton "S'inscrire"
  onSubmit(): void {

    // Petite validation front : tous les champs sont obligatoires
    if (!this.firstName || !this.lastName || !this.adresse || !this.codePostal ||
        !this.ville || !this.email || !this.password) {
      this.errorMessage = "Tous les champs sont obligatoires.";
      return;
    }
    if (!this.email.includes('@')) {
      this.errorMessage = "Adresse email invalide.";
      return;
    }

    // Tu crées un objet JSON avec les données saisies par l’utilisateur.
    const registerData = {
      firstName : this.firstName,
      lastName  : this.lastName,
      adresse   : this.adresse,
      codePostal: this.codePostal,
      ville     : this.ville,
      email     : this.email,
      password  : this.password
    };

    /**Tu envoies cet objet à l’API avec une requête POST vers /api/register.
     * Tu utilises .subscribe() pour réagir à la réponse : */
    this.http.post('http://localhost:8080/api/register', registerData).subscribe({
      /**next s'exécute si la requête a réussi (HTTP 200 OK) :
       * Tu affiches un message de succès
       * Puis tu rediriges l’utilisateur vers la page /login après 2 secondes */
      next: () => {
        this.successMessage = "Inscription réussie ! Redirection en cours…";
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },

      /**error s’exécute si la requête échoue
       * (par ex : e-mail déjà utilisé, serveur indisponible…)
       * Tu affiches un message d’erreur et log l’erreur dans la console */
      error: (error) => {
        this.errorMessage = "Inscription échouée. Vérifiez vos informations ou réessayez.";
        console.error(error);
      }
    });
  }
}
