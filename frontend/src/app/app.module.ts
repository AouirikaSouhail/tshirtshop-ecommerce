import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { ProduitListComponent } from './components/produit-list/produit-list.component';
import { ProduitDetailComponent } from './components/produit-detail/produit-detail.component';
import { LoginComponent } from './components/login/login.component';
import { FormsModule } from '@angular/forms';
import { RegisterComponent } from './components/register/register.component';
import { ProfileEditComponent } from './components/profile-edit/profile-edit.component';



@NgModule({
  declarations: [// tes composants
    AppComponent,
    CategoryListComponent,
    ProduitListComponent,
    ProduitDetailComponent,
    LoginComponent,
    RegisterComponent,
    ProfileEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

/**HttpClient est la classe que j’utilise dans mes services pour faire des appels HTTP.
Mais pour qu’Angular sache comment créer un objet HttpClient, je dois importer HttpClientModule dans app.module.ts.
Sans ce module, Angular ne connaît pas HttpClient et ne peut pas l’injecter automatiquement. */