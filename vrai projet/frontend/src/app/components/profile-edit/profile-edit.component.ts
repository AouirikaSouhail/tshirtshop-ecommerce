//Permet de créer un composant Angular, et d’utiliser le hook ngOnInit() pour exécuter du code au chargement.
//OnInit : interface qui permet d’utiliser ngOnInit(), exécuté automatiquement après l'initialisation du composant.
import { Component,OnInit } from '@angular/core';
// Sert à faire des appels HTTP (GET, PUT...) vers ton backend Spring Boot.
import { HttpClient } from '@angular/common/http';
// ActivatedRoute permet de récupérer l’ID dans l’URL (ex : /profile-edit/3)
// Router permet de naviguer vers une autre page en Angular (ex : redirection après la mise à jour)
import { ActivatedRoute,Router } from '@angular/router';

// Déclaration d’une interface User pour typer les données utilisateur
export interface User {
  name: string;  // Le nom complet de l’utilisateur
  email: string;  // Son adresse e-mail
  password: string; // Son mot de passe (⚠️ à ne jamais stocker en clair dans une vraie app)
}

// Décoration du composant
// Déclare que ce composant s’appelle app-profile-edit, utilise un template HTML et un style CSS liés.

@Component({
  selector: 'app-profile-edit',
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.css']
})



//Composant TypeScript principal, qui va être chargé quand l’utilisateur navigue vers la page d’édition de profil.
export class ProfileEditComponent implements OnInit {

  /**  id!: number;
   Signifie : "je promets que cette variable sera bien initialisée plus tard", même si TypeScript ne le voit pas tout de suite.
   Tu utilises ça quand tu sais que la valeur sera assignée avant d’être utilisée, même si le compilateur ne peut pas le deviner. 
   */
 
  /** id : identifiant de l’utilisateur récupéré dans l’URL
    name, email, password : champs du formulaire
    successMessage, errorMessage : messages à afficher
   */
  //Champs de données (propriétés)
  id! : number;
  name : string = '';
  email : string = '';
  password : string = '';
  successMessage : string = '';
  errorMessage : string = '';

  /**Constructeur (Injection de dépendances) 
   * route → lire l'ID dans l’URL || http → faire des requêtes HTTP vers le backend || router → redirection vers une autre page
   */

  constructor(private route : ActivatedRoute, private http : HttpClient, private router : Router){}

  //Méthode ngOnInit()
  ngOnInit(): void {
    //Dès que la page est chargée, on extrait l’ID depuis l’URL (ex: /profile-edit/3 → id = 3)
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    // On envoie une requête GET au backend pour pré-remplir le formulaire avec les données de l'utilisateur
    this.http.get<User>(`http://localhost:8080/api/user/${this.id}`).subscribe({
      next : (data) =>{      // Si la requête réussit
        this.name = data.name;    // On remplit le champ name
        this.email = data.email;  // On remplit le champ email
        this.password = data.password; // On remplit le champ password (mauvaise pratique en réalité)
        },
      error : (error) => {  // Si une erreur survient
        this.errorMessage = "Impossible de charger les informations utilisateur!";
        console.error(error); // Affiche l’erreur dans la console pour debug
      }
    });
  }
     // Méthode appelée lorsque l'utilisateur clique sur "Mettre à jour"
     onSubmit() : void{

      // On crée un objet avec les données modifiées à envoyer au backend

       const updatedData = {
      name: this.name,    // Nom mis à jour
      email: this.email,      // Email mis à jour
      password: this.password // Mot de passe mis à jour
    };

    // On envoie une requête PUT au backend pour mettre à jour l'utilisateur

    this.http.put(`http://localhost:8080/api/user/${this.id}`,updatedData).subscribe({
      next : () => {
        this.successMessage = 'Profil mis à jour avec succès !'; // Message positif
        // Redirection vers /categories après 2 secondes
        setTimeout (() => this.router.navigate(['/categories']), 2000);
      },
      error : (error) => { // En cas d’erreur de mise à jour
        this.errorMessage = error.error?.error || "Une erreur est survenue lors de la mise à jour.";
 // Message d’erreur personnalisé
        console.error(error); // Affichage en console
      }
    });

    /** Le error ici est un objet HttpErrorResponse généré automatiquement par Angular lorsque la requête échoue.
        Ce HttpErrorResponse contient plusieurs propriétés, par exemple :Propriété	Description
        status	Code HTTP (ex. 400, 404, 500)  message	Message générique d'erreur
        error	Le corps (body) de la réponse renvoyée par le serveur (souvent en texte ou JSON)
    */
  }


}
