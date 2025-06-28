import { Component } from '@angular/core';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls : ['./confirmation.component.css']
})
export class ConfirmationComponent {
  message: string = "✅ Merci pour votre commande ! Un e-mail de confirmation vous sera envoyé si nécessaire.";
}
