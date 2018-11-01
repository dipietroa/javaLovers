
import {throwError as observableThrowError, Observable } from 'rxjs';
import { tap, catchError } from 'rxjs/operators'
import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { AuthConsts } from '../consts/auth.consts';



@Injectable()
export class RefreshTokenInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(req: HttpRequest<any>,
            next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          localStorage.removeItem(AuthConsts.LOCAL_STORAGE_TOKEN);
          localStorage.setItem(AuthConsts.LOCAL_STORAGE_TOKEN, event.headers.get(AuthConsts.TOKEN_HEADER));
          return event;
        } else if (event instanceof HttpErrorResponse) {
          console.log("other error");
        }
      }),
      catchError((err, event) => {
        if (err.status === 401) {
          localStorage.removeItem(AuthConsts.LOCAL_STORAGE_TOKEN);
          localStorage.removeItem(AuthConsts.LOCAL_STORAGE_USER);
        }
        return observableThrowError(err);
      })
    )
  }
}