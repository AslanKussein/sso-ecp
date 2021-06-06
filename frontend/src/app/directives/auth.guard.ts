import {Injectable} from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, ActivatedRoute} from '@angular/router';
import {ErrorInterceptor} from "./error.interceptor";
import {Util} from "../service/util";
import {AuthenticationService} from "../service/authentication.service";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

  constructor(private router: Router,
              private util: Util,
              private activatedRoute: ActivatedRoute,
              private errorInterceptor: ErrorInterceptor,
              private authenticationService: AuthenticationService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    const currentUser = this.authenticationService.currentUserValue
    if (currentUser) {
      return true
    } else {
      if (!this.util.isNullOrEmpty(localStorage.chooseYourSoft)) {
        localStorage.clear();
        this.router.navigate(['/login'])
        return;
      }
      if (localStorage.length == 0) {
        this.router.navigate(['/login'])
        return;
      }
      if (localStorage.getItem('action') != 'logout') {
        this.errorInterceptor.showAuthModal()
      } else {
        this.router.navigate(['/login'])
      }
      return false
    }
  }
}
