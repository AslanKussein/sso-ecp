import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpRequest} from '@angular/common/http';
import {BehaviorSubject, Observable, Subscription, throwError} from 'rxjs';
import {catchError, first, map, tap} from 'rxjs/operators';
import {ConfigService} from "./config.service";
import {User} from "../models/users";
import {Util} from "./util";
import {NotificationService} from "./notification.service";
import {TranslateService} from "@ngx-translate/core";
import {UserService} from "./user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {OpenApiService} from "./open-api.service";

@Injectable({providedIn: 'root'})
export class AuthenticationService implements OnDestroy {

  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;
  private readonly REFRESH_TOKEN = 'refreshToken';
  private readonly JWT_TOKEN = 'JWT_TOKEN';
  subscriptions: Subscription = new Subscription();

  options = {
    headers: new HttpHeaders().set('Content-Type', 'application/json')
      .set('mode', 'no-cors')
  }

  constructor(private http: HttpClient,
              private configService: ConfigService,
              private util: Util,
              private userService: UserService,
              private openApiService: OpenApiService,
              private ngxLoader: NgxUiLoaderService,
              private activatedRoute: ActivatedRoute,
              private notifyService: NotificationService,
              public translate: TranslateService) {
    this.currentUserSubject = new BehaviorSubject<User>(this.util.getCurrentUser());
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  update() {
    this.currentUserSubject = new BehaviorSubject<User>(this.util.getCurrentUser());
    this.currentUser = this.currentUserSubject.asObservable();
  }

  counter: number = 5;

  getCounter(loginForm: any) {
    this.subscriptions.add(this.openApiService.getCounter(loginForm?.value.username).subscribe(data => {
      this.counter = 5 - data.failurecounter;

      if (this.counter == 0) {
        this.notifyService.showWarning("Ваша учетка заблокирована обратитесь к администратору", "");
        return;
      }

      this.ngxLoader.startBackground();

      this.options.headers.set('lang', <string>this.util.getItem('lang'));
      this.subscriptions.add(this.loginIDP(loginForm?.value)
        .pipe(first())
        .subscribe(
          param_ => {
            this.subscriptions.add(this.userService.findUserByLogin().subscribe(data => {
              this.ngxLoader.stopBackground();

              if (data != null) {
                this.counter = 5;
                param_.fullName = data.fullName
                param_.username = data.username
                param_.empId = data.empId
                param_.branch = data.branch
                param_.personDto = data.personDto
                param_.email = data.email
                param_.phone = data.phone
                param_.isAdmin = data.isAdmin
                localStorage.setItem('currentUser', JSON.stringify(param_));
                this.util.dnHref('/home')
              }
            }, () => {
              this.ngxLoader.stopBackground();
            }));
          },
          res => {
            this.ngxLoader.stopBackground();
            this.notifyService.showError('', res)
          }));
    }, () => {
      this.ngxLoader.stopBackground();
    }));
  }

  login(loginForm: any) {
    this.getCounter(loginForm);
  }

  loginIDP(loginForm: any) {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json')
    headers = headers.set('lang', this.util.getCurLang());

    return this.http.post<any>(`${this.configService.authUrl}`.concat('/login'), {
      username: loginForm.username,
      password: loginForm.password,
      certificate: loginForm.certificate
    }, {headers: headers}).pipe(map(user => {
      if (user && user.accessToken) {
        this.storeTokens(user);
        this.util.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
      }
      return user;
    }));
  }

  refreshToken() {
    let headers = new HttpHeaders();
    headers = headers.set('Content-Type', 'application/json')
    headers = headers.set('lang', this.util.getCurLang());

    this.options.headers.set('lang', this.util.getCurLang());
    return this.http.post<any>(`${this.configService.authUrl}`.concat('/refreshToken'), {
      refreshToken: <string>this.getRefreshToken(),
      accessToken: <string>this.getJwtToken()
    }, {headers: headers})
      .pipe(tap((tokens: User) => {
          this.storeTokens(tokens);
        },
        res => {
          this.logout();
        }));
  }

  logout() {
    try {
      this.subscriptions.add(this.signOut().pipe(first())
        .subscribe()
      )
    } finally {
      // @ts-ignore
      this.currentUserSubject.next(null);
      let systemLang = this.util.getItem('lang');
      this.util.dnHref('/login');
      localStorage.clear();
      this.util.setItem('lang', systemLang == null ? 'kz' : systemLang);
    }
  }

  signOut() {
    return this.http.post<any>(`${this.configService.apiUrl}`.concat('/token/logout'), {}, this.options)
      .pipe(tap(a => {
      }));
  }

  getJwtToken() {
    return this.util.getItem(this.JWT_TOKEN);
  }

  public storeTokens(tokens: User) {
    this.util.setItem(this.JWT_TOKEN, <string>tokens.accessToken);
    this.util.setItem(this.REFRESH_TOKEN, <string>tokens.refreshToken);
  }

  private getRefreshToken() {
    return this.util.getItem(this.REFRESH_TOKEN);
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
