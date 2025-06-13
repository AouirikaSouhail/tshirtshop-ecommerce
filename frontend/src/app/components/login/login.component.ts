//importes Component d’Angular → c’est ce qui permet de créer un composant.
import { Component } from '@angular/core';
//Tu importes HttpClient, qui permet à Angular de faire des requêtes HTTP vers ton backend Spring Boot.
import { HttpClient } from '@angular/common/http';
//Tu importes Router, qui permet de naviguer d’une page à une autre en Angular.
import { Router } from '@angular/router';

//Tu déclares ton composant Angular :
@Component({
  selector: 'app-login',  // selector : nom du composant (<app-login> dans HTML)
  templateUrl: './login.component.html',  //templateUrl : chemin du fichier HTML associé
  styleUrls: ['./login.component.css']      //styleUrls : style CSS associé à ce composant
})

//Tu crées la classe LoginComponent qui contient la logique du formulaire de connexion
export class LoginComponent {

  email: string = '';
  password: string = '';
  errorMessage: string = '';
  
  //Le constructeur injecte deux outils : HttpClient : pour envoyer des données vers le backend
  //Router : pour rediriger l’utilisateur vers une autre page après connexion
  constructor(private http: HttpClient, private router: Router){}

  //Cette méthode est appelée quand on clique sur le bouton "Se connecter" dans le formulaire HTML.

  onSubmit(): void{
    //Tu crées un objet loginData avec les champs email et password tapés par l’utilisateur.
          const loginData ={
           email : this.email,
           password: this.password
          };

    // Tu fais une requête POST vers ton backend Spring Boot à l’URL /api/login, avec les données du formulaire.
          this.http.post('http://localhost:8080/api/login',loginData).subscribe({
            //Si le login fonctionne, tu rediriges l’utilisateur vers la page /categories.
            next : () =>{this.router.navigate(['/categories'])}, //redirection après succès
            error : (error) => {
                               this.errorMessage = "Email ou mot de passe incorrect.";
                               console.error(error);//Si le login échoue (ex. mauvais mot de passe), tu affiches un message d’erreur.
                              }
          });
  }
}
