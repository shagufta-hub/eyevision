import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { API_CONFIG } from '../../api.config';

@Injectable({
  providedIn: 'root'
})
export class OrdertrackingserviceService {

  private apiUrl = `${API_CONFIG.baseUrl}/orders`;

  constructor(private http: HttpClient) { }

  // placeOrder(order: PlaceOrder): Observable<PlaceOrder> {
  //   return this.http.post<PlaceOrder>(`${this.apiUrl}/place`, order);
  // }

  getOrders_statusbyid(orderid: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${orderid}/track`);
  }
}
