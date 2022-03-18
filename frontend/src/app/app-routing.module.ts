import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HorseComponent } from './component/horse/horse.component';
import {HorseEditComponent} from './component/horse-edit/horse-edit.component';
import {HorseDetailsComponent} from './component/horse-details/horse-details.component';
import {HorseCreateComponent} from './component/horse-create/horse-create.component';
import {OwnerCreateComponent} from './component/owner-create/owner-create.component';
import {OwnerComponent} from './component/owner/owner.component';

const routes: Routes = [
  {path: '', redirectTo: 'horses', pathMatch: 'full'},
  {path: 'horses', component: HorseComponent},
  {path: 'horses/create', component: HorseCreateComponent},
  {path: 'horses/:id/edit', component: HorseEditComponent},
  {path: 'horses/:id/details', component: HorseDetailsComponent},
  {path: 'owners', component: OwnerComponent},
  {path: 'owners/create', component: OwnerCreateComponent},
  {path: '**', redirectTo: 'horses'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
