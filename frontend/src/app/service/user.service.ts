import {Injectable} from '@angular/core';
import {ConfigService} from './config.service';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private configService: ConfigService,
              private http: HttpClient) {
  }

  findUserByLogin(): Observable<any> {
    return this.http.post<any>(`${this.configService.apiUrl}/users/getUserByName`, {}).pipe(
      tap(),
      catchError(UserService.handleError)
    );
  }

  private static handleError(error: HttpErrorResponse) {
    return throwError(
      error);
  }
}
