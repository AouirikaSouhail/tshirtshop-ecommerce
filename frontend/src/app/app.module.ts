import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CategoryListComponent } from './components/category-list/category-list.component';
import { ProduitListComponent } from './components/produit-list/produit-list.component';


@NgModule({
  declarations: [// tes composants
    AppComponent,
    CategoryListComponent,
    ProduitListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

/**HttpClient est la classe que j’utilise dans mes services pour faire des appels HTTP.
Mais pour qu’Angular sache comment créer un objet HttpClient, je dois importer HttpClientModule dans app.module.ts.
Sans ce module, Angular ne connaît pas HttpClient et ne peut pas l’injecter automatiquement. */