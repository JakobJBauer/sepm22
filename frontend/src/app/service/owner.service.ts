import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Owner} from '../dto/owner';
import {OwnerSearchParams} from '../dto/ownerSearchParams';

const baseUri = environment.backendUrl + '/owners';

@Injectable({
  providedIn: 'root'
})
export class OwnerService {

  constructor(
    private http: HttpClient
  ) { }

  create(owner: Owner): Observable<Owner> {
    return this.http.post<Owner>(baseUri, owner);
  }

  getAll(searchParams: OwnerSearchParams = {}): Observable<Owner[]> {
    return this.http.get<Owner[]>(baseUri, {params: searchParams as HttpParams});
  }

}
