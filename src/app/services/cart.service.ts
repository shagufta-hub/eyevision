import { Injectable, computed, signal } from '@angular/core';
import {
  FREE_SHIPPING_THRESHOLD,
  SHIPPING_FEE,
} from '../constants/currency.constants';
import { CartItem, Product } from '../models/product.model';
import { cartLineId, getVariantImage } from '../utils/color-variants.util';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CartService {
  baseurl = 'https://evoptical.in';

  private readonly items = signal<CartItem[]>(this.loadFromStorage());  

  readonly cartItems = this.items.asReadonly();

  readonly itemCount = computed(() =>
    this.items().reduce((sum, i) => sum + i.quantity, 0)
  );

  readonly subtotal = computed(() =>
    this.items().reduce((sum, i) => sum + i.product.price * i.quantity, 0)
  );

  readonly shipping = computed(() =>
    this.subtotal() >= FREE_SHIPPING_THRESHOLD ? 0 : SHIPPING_FEE
  );

  readonly total = computed(() => this.subtotal() + this.shipping());

  lineId(item: CartItem): string {
    return cartLineId(item.product.id, item.selectedColor);
  }

  itemImage(item: CartItem): string {
    return getVariantImage(item.product, item.selectedColor);
  }

  addToCart(product: Product, quantity = 1, selectedColor?: string): void {
    const color =
      selectedColor ?? product.variants[0]?.name ?? 'Default';
    const current = [...this.items()];
    const lineKey = cartLineId(product.id, color);
    const existing = current.find(
      (i) => cartLineId(i.product.id, i.selectedColor) === lineKey
    );

    if (existing) {
      existing.quantity += quantity;
    } else {
      current.push({
        product, quantity, selectedColor: color,
        name: '',
        title: '',
        price: 0,
        image: ''
      });
    }

    this.persist(current);
  }

  updateQuantity(lineKey: string, quantity: number): void {
    if (quantity < 1) {
      this.removeItem(lineKey);
      return;
    }

    const current = this.items().map((i) =>
      cartLineId(i.product.id, i.selectedColor) === lineKey
        ? { ...i, quantity }
        : i
    );
    this.persist(current);
  }

  removeItem(lineKey: string): void {
    const current = this.items().filter(
      (i) => cartLineId(i.product.id, i.selectedColor) !== lineKey
    );
    this.persist(current);
  }

  clearCart(): void {
    this.persist([]);
  }

  private persist(items: CartItem[]): void {
    this.items.set(items);
    localStorage.setItem('eyevesion-cart', JSON.stringify(items));
  }

  private loadFromStorage(): CartItem[] {
    try {
      const raw = localStorage.getItem('eyevesion-cart');
      if (!raw) {
        return [];
      }
      const parsed: CartItem[] = JSON.parse(raw);
      return parsed.map((item) => ({
        ...item,
        selectedColor:
          item.selectedColor ??
          item.product.variants?.[0]?.name ??
          'Default',
        product: this.normalizeProduct(item.product),
      }));
    } catch {
      return [];
    }
  }

  /** Restore variants when loading legacy cart JSON that only had `colors`. */
  private normalizeProduct(product: Product & { colors?: string[] }): Product {
    if (product.variants?.length) {
      return product;
    }
    const legacyColors = product.colors ?? ['Default'];
    return {
      ...product,
      variants: legacyColors.map((name: any) => ({
        name,
        hex: '#0d6e6e',
        image: product.image,
      })),
    };
  }


}
