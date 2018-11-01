import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthConsts } from '../consts/auth.consts';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>,
            next: HttpHandler): Observable<HttpEvent<any>> {
      const idToken = localStorage.getItem(AuthConsts.LOCAL_STORAGE_TOKEN);
      let authReq = req;
      if (idToken != null && idToken != 'null') {
        authReq = authReq.clone({
            headers: authReq.headers.append(AuthConsts.TOKEN_HEADER, idToken)
        });
      }
      return next.handle(authReq);
  }
}