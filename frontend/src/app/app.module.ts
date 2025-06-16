import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'; // ← Ajouté HTTP_INTERCEPTORS
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { ProduitListComponent } from './components/produit-list/produit-list.component';
import { ProduitDetailComponent } from './components/produit-detail/produit-detail.component';
import { LoginComponent } from './components/login/login.component';
import { FormsModule } from '@angular/forms';
import { RegisterComponent } from './components/register/register.component';
import { ProfileEditComponent } from './components/profile-edit/profile-edit.component';
import { AuthInterceptor } from './auth.interceptor';
import { HeaderComponent } from './components/header/header.component'; // ← Bien placé

@NgModule({
  declarations: [
    AppComponent,
    CategoryListComponent,
    ProduitListComponent,
    ProduitDetailComponent,
    LoginComponent,
    RegisterComponent,
    ProfileEditComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
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
