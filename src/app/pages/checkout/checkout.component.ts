import { NgIf, NgFor } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { InrCurrencyPipe } from '../../pipes/inr-currency.pipe';
import { CartService } from '../../services/cart.service';
import { PlaceOrderService } from '../../services/place-order.service';
import { PlaceOrder } from '../../models/place-order.model';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, InrCurrencyPipe, RouterLink],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
})
export class CheckoutComponent {

  private readonly cart = inject(CartService);
  private readonly router = inject(Router);
  private readonly placeOrderService = inject(PlaceOrderService);

  readonly cartService = this.cart;

  orderPlaced = false;
  loading = false;
  errorMessage = '';
  orderId: number | null = null;
  selectedPaymentMethod = 'credit_card';

  paymentMethods = [
    { id: 'credit_card', label: 'Credit/Debit Card', icon: '💳' },
    { id: 'upi', label: 'UPI', icon: '📱' },
    { id: 'cod', label: 'Cash on Delivery', icon: '💵' },
  ];

  form = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    address: '',
    city: '',
    zip: '',
    cardNumber: '',
    expiry: '',
    cvv: '',
    upiId: '',
  };

  placeOrder(): void {
    if (this.cart.cartItems().length === 0) {
      return;
    }

    // Validate form
    if (!this.validateForm()) {
      this.errorMessage = 'Please fill all required fields correctly';
      return;
    }

    this.loading = true;

    // Get first product from cart
    const firstItem = this.cart.cartItems()[0];
    const productName = firstItem?.name || 'Multiple Products';
    const totalQuantity = this.cart.cartItems().reduce((sum, item) => sum + (item.quantity || 1), 0);
    const totalPrice = this.cart.total();

    // Get payment method label
    const paymentMethodLabel = this.paymentMethods.find(m => m.id === this.selectedPaymentMethod)?.label || 'Credit Card';

    // Create order object
    const order: PlaceOrder = {
      customerName: `${this.form.firstName} ${this.form.lastName}`,
      email: this.form.email,
      phoneNumber: this.form.phone,
      productName: productName,
      quantity: totalQuantity,
      totalPrice: totalPrice,
      shippingAddress: this.form.address,
      city: this.form.city,
      zipCode: this.form.zip,
      paymentMethod: paymentMethodLabel
    };

    // Call API
    this.placeOrderService.placeOrder(order).subscribe({
      next: (response) => {
        this.loading = false;
        this.orderPlaced = true;
        this.orderId = response.orderId || null;
        this.cart.clearCart();
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error?.error?.message || 'Failed to place order. Please try again.';
        console.error('Error placing order:', error);
      }
    });
  }

  private validateForm(): boolean {
    const baseValidation = !!(
      this.form.firstName &&
      this.form.lastName &&
      this.form.email &&
      this.form.phone &&
      this.form.address &&
      this.form.city &&
      this.form.zip
    );

    if (!baseValidation) return false;

    // Validate based on payment method
    if (this.selectedPaymentMethod === 'credit_card') {
      return !!(this.form.cardNumber && this.form.expiry && this.form.cvv);
    } else if (this.selectedPaymentMethod === 'upi') {
      return !!this.form.upiId;
    }

    return true; // COD doesn't need additional validation
  }
 
}
