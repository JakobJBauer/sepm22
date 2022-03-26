import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {BasicHorse} from '../dto/basicHorse';
import {HorseSearchParams} from "../dto/horseSearchParams";
import {FullHorse} from "../dto/fullHorse";
import {OutputHorse} from "../dto/outputHorse";
import {ParentSearchParams} from "../dto/parentSearchParams";
import {ParentHorse} from "../dto/parentHorse";
import {AncestorHorse} from "../dto/ancestorHorse";

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
  getAll(searchParams: HorseSearchParams): Observable<BasicHorse[]> {
    return this.http.get<BasicHorse[]>(baseUri, {params: searchParams as HttpParams});
  }

  getAncestorTree(maxGeneration: number): Observable<AncestorHorse[]> {
    return this.http.get<AncestorHorse[]>(baseUri + '/ancestor-tree', {params: {maxGeneration}});
  }

  getParentOptions(searchParams: ParentSearchParams): Observable<ParentHorse[]> {
    return this.http.get<ParentHorse[]>(baseUri + '/parentsearch', {params: searchParams as HttpParams});
  }

  getById(id: number): Observable<FullHorse> {
    return this.http.get<FullHorse>(baseUri + '/' + id.toString());
  }

  create(horse: OutputHorse): Observable<BasicHorse> {
    return this.http.post<BasicHorse>(baseUri, horse);
  }

  update(horse: OutputHorse, id: number): Observable<FullHorse> {
    return this.http.put<FullHorse>(baseUri + '/' + id.toString(), horse);
  }

  deleteById(id: number): Observable<any> {
    return this.http.delete(baseUri + '/' + id.toString());
  }
}
