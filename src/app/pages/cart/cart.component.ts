import { NgFor, NgIf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FREE_SHIPPING_THRESHOLD } from '../../constants/currency.constants';
import { InrCurrencyPipe } from '../../pipes/inr-currency.pipe';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [NgFor, NgIf, InrCurrencyPipe, RouterLink],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent {
  readonly cart = inject(CartService);
  readonly freeShippingMin = FREE_SHIPPING_THRESHOLD;
}
