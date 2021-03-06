import { Component } from '@angular/core';
import { AuthConsts } from './login/consts/auth.consts';
import { CookieHandler } from './login/utils/cookie-handler.utils';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  requestLogin = false;

  openLogin() : void {
    this.requestLogin = !this.requestLogin;
  }

  closeLogin() : void {
    this.requestLogin = false;
  }

  getCurUser() : string {
    return localStorage.getItem(AuthConsts.LOCAL_STORAGE_USER);
  }
  
  isLogged() : boolean {
    try {
      return localStorage.getItem(AuthConsts.LOCAL_STORAGE_TOKEN) != null &&
      localStorage.getItem(AuthConsts.LOCAL_STORAGE_TOKEN) != 'null'
    } catch (err) {
      CookieHandler.init();
      return false;
    }
  }

  disconnect() : void {
    localStorage.removeItem(AuthConsts.LOCAL_STORAGE_TOKEN);
    localStorage.removeItem(AuthConsts.LOCAL_STORAGE_USER);
  }
}
