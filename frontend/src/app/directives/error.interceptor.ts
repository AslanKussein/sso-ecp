import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {catchError, filter, switchMap, take} from 'rxjs/operators';
import {User} from "../models/users";
import {AuthenticationService} from "../service/authentication.service";
import {ConfigService} from "../service/config.service";
import {NotificationService} from "../service/notification.service";
import {Util} from "../service/util";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {ActivatedRoute} from "@angular/router";

@Injectable({providedIn: 'root'})
export class ErrorInterceptor implements HttpInterceptor {

  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private authenticationService: AuthenticationService,
              private configService: ConfigService,
              private notificationService: NotificationService,
              private util: Util,
              // private modalService: BsModalService,
              private ngxLoader: NgxUiLoaderService,
              private activatedRoute: ActivatedRoute) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      this.ngxLoader.stop();
      if (err.status === 401 && !window.location.href.includes('login')) {
        return this.handle401Error(request, next);
      } else if (err.status === 400) {
        if (err.url.includes(this.configService.authUrl)) {
          if (err.error.error_description.includes('Refresh token expired')) {
            this.authenticationService.logout();
            location.reload(true);
          } else {
            // if (!['login'].includes(this.activatedRoute.snapshot['_routerState'].url.split(";")[0].replace('/', ''))) {
            //   this.showAuthModal();
            // }
          }
        }
      }
      if (err.status != 400 && !this.util.isNullOrEmpty(err.error.message?.ru)) {
        this.notificationService.showInfo('Информация', err.error.message[this.util.getError()])
      }

      const error = err.error.message || err.statusText;
      return throwError(error);
    }))
  }


  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {

    if (!this.isRefreshing) {

      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authenticationService.refreshToken().pipe(
        switchMap((token: any) => {
          this.isRefreshing = false;

          let persons = JSON.parse(<string>localStorage.getItem('currentUser'));

          persons.accessToken = token.accessToken;
          persons.refreshToken = token.refreshToken;
          localStorage.setItem("currentUser", JSON.stringify(persons));
          this.authenticationService.update();
          this.refreshTokenSubject.next(token.accessToken);
          this.authenticationService.storeTokens(token);
          request = request.clone({
            setHeaders: {
              Authorization: `Bearer ${token.accessToken}`,
              lang: this.util.getCurLang()
            }
          });
          return next.handle(request);
        }));

    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap(jwt => {
          request = request.clone({
            setHeaders: {
              Authorization: `Bearer ${jwt}`,
              lang: this.util.getCurLang()
            }
          });
          return next.handle(request);
        }));
    }
  }

  showAuthModal() {
    // this.modalService.show(LoginModalComponent, {
    //   class: 'modal-md',
    //   initialState: {
    //     centered: true
    //   }
    // });
  }
}
