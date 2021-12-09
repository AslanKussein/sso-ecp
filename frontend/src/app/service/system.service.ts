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

  public create(data: any): Observable<any> {
    return this.http.post(`${this.configService.apiUrl}/systems/create`, data)
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public edit(id: number, data: any): Observable<any> {
    return this.http.put(`${this.configService.apiUrl}/systems/${id}`, data)
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public remove(id: number): Observable<any> {
    return this.http.delete(`${this.configService.apiUrl}/systems/remove/${id}`, {})
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public redirectSystem(data: any): Observable<any> {
    return this.http.post(`${this.configService.apiUrl}/systems/redirectSystem`, data)
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public getDAlias(): Observable<any> {
    return this.http.get(`${this.configService.apiUrl}/systems/getDAlias`)
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public getBlockUserList(): Observable<any> {
    return this.http.get(`${this.configService.apiUrl}/systems/getBlockUserList`)
      .pipe(
        tap(data => {
        }),
        catchError(SystemService.handleError)
      );
  }

  public unBlockUser(empId: number): Observable<any> {
    return this.http.delete(`${this.configService.apiUrl}/systems/unBlockUser/${empId}`)
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
