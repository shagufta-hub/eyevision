import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../components/product-card/product-card.component';
import {
  AUDIENCE_LABELS,
  CATEGORY_LABELS,
  Audience,
  Product,
  ProductCategory,
  ProductFilters,
} from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { getDiscountPercent } from '../../utils/product.utils';

@Component({
  selector: 'app-sale',
  standalone: true,
  imports: [NgFor, NgIf, FormsModule, RouterLink, ProductCardComponent],
  templateUrl: './sale.component.html',
  styleUrls: ['./sale.component.scss'],
})
export class SaleComponent implements OnInit {
  private readonly productService = inject(ProductService);

  products: Product[] = [];
  filters: ProductFilters = {
    category: 'all',
    audience: 'all',
    search: '',
    sort: 'discount',
    onSale: true,
  };

  readonly categoryLabels = CATEGORY_LABELS;
  readonly audienceLabels = AUDIENCE_LABELS;
  readonly getDiscountPercent = getDiscountPercent;

  readonly categories: (ProductCategory | 'all')[] = [
    'all',
    'spectacles',
    'goggles',
    'contact-lenses',
  ];
  readonly audiences: (Audience | 'all')[] = ['all', 'women', 'men', 'kids'];

  ngOnInit(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    this.products = this.productService.filter(this.filters);
  }

  getCategoryLabel(key: ProductCategory | 'all'): string {
    return key === 'all' ? 'All Categories' : this.categoryLabels[key];
  }

  getAudienceLabel(key: Audience | 'all'): string {
    return key === 'all' ? 'Everyone' : this.audienceLabels[key];
  }

  get maxDiscount(): number {
    const sale = this.productService.getOnSale();
    if (!sale.length) {
      return 0;
    }
    return Math.max(...sale.map((p) => getDiscountPercent(p)));
  }
}
