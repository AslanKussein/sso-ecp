import {Injectable} from '@angular/core';
import {ConfigService} from './config.service';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SystemService {

  constructor(private configService: ConfigService,
              private http: HttpClient) {
  }

  public getSystemList(): Observable<any> {
    return this.http.get(`${this.configService.apiUrl}/systems/getSystemList`, {})
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public getAll(): Observable<any> {
    return this.http.get(`${this.configService.apiUrl}/systems/all`, {})
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public edit(id: number, data: any): Observable<any> {
    return this.http.put(`${this.configService.apiUrl}/systems/`.concat(id.toLocaleString()), data)
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  private static handleError(error: HttpErrorResponse) {
    if (error instanceof ErrorEvent) {
      console.error('An error occurred:', error);
    } else {
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.message}`);
    }
    return throwError(
      error);
  }
}
