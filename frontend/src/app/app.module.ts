import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'; // ← Ajouté HTTP_INTERCEPTORS
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { ProduitListComponent } from './components/produit-list/produit-list.component';
import { ProduitDetailComponent } from './components/produit-detail/produit-detail.component';
import { LoginComponent } from './components/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './components/register/register.component';
import { ProfileEditComponent } from './components/profile-edit/profile-edit.component';
import { AuthInterceptor } from './auth.interceptor';
import { HeaderComponent } from './components/header/header.component';
import { RgpdComponent } from './components/rgpd/rgpd.component';
import { CgvComponent } from './components/cgv/cgv.component';
import { ContactComponent } from './components/contact/contact.component';
import { AddProductComponent } from './components/add-product/add-product.component';
import { EditProductComponent } from './components/edit-product/edit-product.component';
import { PanierComponent } from './components/panier/panier.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { ConfirmationComponent } from './components/confirmation/confirmation.component'; // ← Bien placé
import { CommonModule } from '@angular/common';
import { MesCommandesComponent } from './components/mes-commandes/mes-commandes.component';


@NgModule({
  declarations: [
    AppComponent,
    CategoryListComponent,
    ProduitListComponent,
    ProduitDetailComponent,
    LoginComponent,
    RegisterComponent,
    ProfileEditComponent,
    HeaderComponent,
    RgpdComponent,
    CgvComponent,
    ContactComponent,
    AddProductComponent,
    EditProductComponent,
    PanierComponent,
    CheckoutComponent,
   ConfirmationComponent,
   MesCommandesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
