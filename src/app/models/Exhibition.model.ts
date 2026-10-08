export interface ExhibitionRegistrationRequest {
  name: string;
  mobile: string;
  email?: string;
  exhibitionId: number;
}

export interface ExhibitionRegistrationResponse {
  success: boolean;
  message: string;
  customerId: number;
  name: string;
  membershipToken: string;
  alreadyRegistered: boolean;
}

export interface TokenValidationResponse {
  valid: boolean;
  message: string;

  customerId?: number;
  customerName?: string;
  mobile?: string;
  email?: string;

  membershipToken?: string;

  exhibitionId?: number;
  exhibitionName?: string;

  eyeTestCompleted?: boolean;
  freeSpecsClaimed?: boolean;

  discountPercentage?: number;
}