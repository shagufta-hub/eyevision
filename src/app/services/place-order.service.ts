import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlaceOrder } from '../models/place-order.model';

import { API_CONFIG } from '../api.config';

@Injectable({
  providedIn: 'root'
})
export class PlaceOrderService {

  private apiUrl = `${API_CONFIG.baseUrl}/orders`;

  constructor(private http: HttpClient) { }

  placeOrder(order: PlaceOrder): Observable<PlaceOrder> {
    return this.http.post<PlaceOrder>(`${this.apiUrl}/place`, order);
  }

  getAllOrders(): Observable<PlaceOrder[]> {
    return this.http.get<PlaceOrder[]>(`${this.apiUrl}/all`);
  }

  getOrderById(orderId: number): Observable<PlaceOrder> {
    return this.http.get<PlaceOrder>(`${this.apiUrl}/${orderId}`);
  }

  getOrdersByCustomer(customerName: string): Observable<PlaceOrder[]> {
    return this.http.get<PlaceOrder[]>(`${this.apiUrl}/customer/${customerName}`);
  }

  getOrdersByEmail(email: string): Observable<PlaceOrder[]> {
    return this.http.get<PlaceOrder[]>(`${this.apiUrl}/email/${email}`);
  }

  updateOrder(orderId: number, order: PlaceOrder): Observable<PlaceOrder> {
    return this.http.put<PlaceOrder>(`${this.apiUrl}/${orderId}`, order);
  }

  deleteOrder(orderId: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${orderId}`);
  }

  getTotalOrderCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }
}
