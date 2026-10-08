import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  ExhibitionRegistrationRequest,
  ExhibitionRegistrationResponse,
  TokenValidationResponse
} from '../models/Exhibition.model';

import { API_CONFIG } from '../api.config';

@Injectable({
  providedIn: 'root'
})
export class ExhibitionService {

  private readonly baseUrl =
    `${API_CONFIG.baseUrl}/exhibition`;

  private readonly baseUrlAdmin =
    `${API_CONFIG.baseUrl}/admin/exhibitions`;

  constructor(
    private http: HttpClient
  ) {}



  // ==========================================
  // REGISTER CUSTOMER
  // ==========================================

  registerCustomer(
    request: ExhibitionRegistrationRequest
  ): Observable<ExhibitionRegistrationResponse> {

    return this.http.post<ExhibitionRegistrationResponse>(
      `${this.baseUrl}/register`,
      request
    );
  }

  // ==========================================
  // VALIDATE TOKEN
  // ==========================================

  validateToken(
    token: string,
    exhibitionId: number
  ): Observable<TokenValidationResponse> {

    return this.http.get<TokenValidationResponse>(
      `${this.baseUrl}/token/${token}`,
      {
        params: {
          exhibitionId: exhibitionId.toString()
        }
      }
    );
  }

  // ==========================================
  // COMPLETE EYE TEST
  // ==========================================

  completeEyeTest(
    token: string,
    exhibitionId: number
  ): Observable<TokenValidationResponse> {

    return this.http.post<TokenValidationResponse>(
      `${this.baseUrl}/token/${token}/eye-test`,
      null,
      {
        params: {
          exhibitionId: exhibitionId.toString()
        }
      }
    );
  }

  // ==========================================
  // CLAIM FREE SPECS
  // ==========================================

  claimFreeSpecs(
    token: string,
    exhibitionId: number
  ): Observable<TokenValidationResponse> {

    return this.http.post<TokenValidationResponse>(
      `${this.baseUrl}/token/${token}/free-specs`,
      null,
      {
        params: {
          exhibitionId: exhibitionId.toString()
        }
      }
    );
  }

  getExhibitionDetails(id: number): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrlAdmin}/${id}`
    );
  }
}