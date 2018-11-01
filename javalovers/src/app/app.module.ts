import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { MainPageComponent } from './main-page/main-page.component';
import { CommentsService } from './api/comments.service';
import { HttpClientModule } from '@angular/common/http';
import { CardsComponent } from './cards/cards.component';
import { CommentsFormComponent } from './comments-form/comments-form.component';
import { ReversePipe } from './pipes/reverse.pipe';
import { LoginComponent } from './login/login.component';
import { AuthService } from './api/auth.service';
import { HTTP_INTERCEPTORS  } from '@angular/common/http';
import { AuthInterceptor } from './login/interceptors/authentication.interceptor';
import { RefreshTokenInterceptor } from './login/interceptors/refreshToken.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    CardsComponent,
    CommentsFormComponent,
    ReversePipe,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: RefreshTokenInterceptor, multi: true },
    CommentsService,
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
