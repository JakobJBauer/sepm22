import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './component/header/header.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {HorseComponent} from './component/horse/horse.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { HorseEditComponent } from './component/horse-edit/horse-edit.component';
import { HorseDetailsComponent } from './component/horse-details/horse-details.component';
import { HorseCreateComponent } from './component/horse-create/horse-create.component';
import { HorseFormComponent } from './component/horse-form/horse-form.component';
import { OwnerCreateComponent } from './component/owner-create/owner-create.component';
import { OwnerComponent } from './component/owner/owner.component';
import { HorseAncestorComponent } from './component/horse-ancestor/horse-ancestor.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HorseComponent,
    HorseEditComponent,
    HorseDetailsComponent,
    HorseCreateComponent,
    HorseFormComponent,
    OwnerCreateComponent,
    OwnerComponent,
    HorseAncestorComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
