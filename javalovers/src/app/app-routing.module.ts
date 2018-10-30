import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes, Router } from '@angular/router';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { MainPageComponent } from './main-page/main-page.component';

const routes: Routes = [
  { path: 'admin', component: AdminPageComponent },
  { path: '', component: MainPageComponent }
]

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [ RouterModule ],
  declarations: []
})
export class AppRoutingModule { }
