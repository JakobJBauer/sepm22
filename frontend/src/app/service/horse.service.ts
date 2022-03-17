import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Horse} from '../dto/horse';
import {SearchParams} from "../dto/searchParams";

const baseUri = environment.backendUrl + '/horses';

@Injectable({
  providedIn: 'root'
})
export class HorseService {

  constructor(
    private http: HttpClient,
  ) { }

  /**
   * Get all horses stored in the system
   *
   * @return observable list of found horses.
   */
  getAll(searchParams: SearchParams): Observable<Horse[]> {
    return this.http.get<Horse[]>(baseUri, {params: searchParams as HttpParams});
  }

  getById(id: number): Observable<Horse> {
    return this.http.get<Horse>(baseUri + '/' + id.toString());
  }

  create(horse: Horse): Observable<Horse> {
    return this.http.post<Horse>(baseUri + '/create', horse);
  }

  update(horse: Horse, id: number): Observable<Horse> {
    return this.http.put<Horse>(baseUri + '/' + id.toString(), horse);
  }

  deleteById(id: number): Observable<any> {
    return this.http.delete(baseUri + '/' + id.toString());
  }
}
