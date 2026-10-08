import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_CONFIG } from '../api.config';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl = `${API_CONFIG.baseUrl}/orders`;

  constructor(private http: HttpClient) {}

  // सभी orders लाओ
  getAllOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`);
  }

  // Order details लाओ
  getOrderById(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${orderId}`);
  }

  // Order status update करो
  updateOrderStatus(orderId: number, status: string): Observable<any> {
    const orderData = { status: status };
    return this.http.put(`${this.apiUrl}/${orderId}`, orderData);
  }

  // Tracking history लाओ
  getTrackingHistory(orderId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${orderId}/tracking-history`);
  }
}
