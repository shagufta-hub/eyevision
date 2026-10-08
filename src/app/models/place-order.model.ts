export interface PlaceOrder {
  orderId?: number;
  customerName: string;
  email: string;
  phoneNumber: string;
  productName: string;
  quantity: number;
  totalPrice: number;
  shippingAddress: string;
  city: string;
  zipCode: string;
  paymentMethod: string;
  status?: string;
  createdAt?: Date;
  updatedAt?: Date;
}
