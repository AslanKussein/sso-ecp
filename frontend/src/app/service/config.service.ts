import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  env!: any;

  // serverAddress = window.location.origin;
  // apiUrl = '/sso/api';
  // openApiUrl = '/sso/open-api';
  // authUrl = '/sso/open-api/auth';
  apiUrl = 'http://localhost:8080/sso/api';
  openApiUrl = 'http://localhost:8080/sso/open-api';
  authUrl = 'http://localhost:8080/sso/open-api/auth';

  constructor() {
    if (environment.production) {
      this.apiUrl = '/sso/api';
      this.openApiUrl = '/sso/open-api';
      this.authUrl = '/sso/open-api/auth';
    }
  }
}
