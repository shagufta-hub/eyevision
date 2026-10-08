import { NgFor, NgIf } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import {
  AUDIENCE_LABELS,
  CATEGORY_LABELS,
  ColorVariant,
  Product,
} from '../../models/product.model';
import { FREE_SHIPPING_THRESHOLD } from '../../constants/currency.constants';
import { InrCurrencyPipe } from '../../pipes/inr-currency.pipe';
import { CartService } from '../../services/cart.service';
import { ProductService } from '../../services/product.service';
import { ShopService } from 'src/app/services/shop.service';
@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [NgIf, NgFor, InrCurrencyPipe, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly productService = inject(ProductService);
  private readonly shopService = inject(ShopService);
  private readonly cart = inject(CartService);

  private routeSub?: Subscription;

  product?: Product;
  quantity = 1;
  selectedColor = '';
  displayImage = '';
  added = false;

  readonly categoryLabels = CATEGORY_LABELS;
  readonly audienceLabels = AUDIENCE_LABELS;
  readonly freeShippingMin = FREE_SHIPPING_THRESHOLD;

  ngOnInit(): void {
    this.routeSub = this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (!id) {
        this.router.navigate(['/shop']);
        return;
      }

      this.shopService.getproductdetails(id).subscribe((product: Product) => {
        this.product = product;
        if (!this.product) {
          this.router.navigate(['/shop']);
          return;
        }

        this.quantity = 1;
        this.added = false;

        if (this.product.variants?.length) {
          this.selectVariant(this.product.variants[0]);
        }
      });
    });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  selectVariant(variant: ColorVariant): void {
    this.selectedColor = variant.name;
    this.displayImage = variant.image;
  }

  get selectedVariant(): ColorVariant | undefined {
    return this.product?.variants.find((v) => v.name === this.selectedColor);
  }

  addToCartpre(): void {
    if (this.product && this.selectedColor) {
      this.cart.addToCart(this.product, this.quantity, this.selectedColor);
      this.added = true;
      setTimeout(() => (this.added = false), 2000);
    }
  }
  addToCart(): void {
  if (!this.product) {
    return;
  }

  const color = this.selectedColor || '';

  this.cart.addToCart(
    this.product,
    this.quantity,
    color
  );

  this.added = true;

  setTimeout(() => {
    this.added = false;
  }, 2000);
}

  getImageUrl(path: string): string {
  if (!path) {
    return 'assets/images/product-placeholder.png';
  }

  if (path.startsWith('http')) {
    return path;
  }

  return `https://evoptical.in${path}`;
}

selectProductImage(imagePath: string): void {
  this.displayImage = this.getImageUrl(imagePath);
}
}
