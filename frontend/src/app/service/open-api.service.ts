import {Injectable} from '@angular/core';
import {ConfigService} from './config.service';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, tap} from "rxjs/operators";
import {Observable, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OpenApiService {

  constructor(private configService: ConfigService,
              private http: HttpClient) {
  }

  getCounter(username: string): Observable<any> {
    return this.http.get<any>(`${this.configService.openApiUrl}/getCounter/${username}`, {}).pipe(
      tap(),
      catchError(OpenApiService.handleError)
    );
  }

  getContacts(): Observable<any> {
    return this.http.get<any>(`${this.configService.openApiUrl}/getContacts`, {}).pipe(
      tap(),
      catchError(OpenApiService.handleError)
    );
  }

  private static handleError(error: HttpErrorResponse) {
    return throwError(
      error);
  }
}
