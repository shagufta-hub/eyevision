import { NgFor } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../components/product-card/product-card.component';
import {
  EYE_EXAM_MIN_ORDER,
  FREE_SHIPPING_THRESHOLD,
} from '../../constants/currency.constants';
import { InrCurrencyPipe } from '../../pipes/inr-currency.pipe';
import { ProductService } from '../../services/product.service';
import {
  AUDIENCE_LABELS,
  CATEGORY_LABELS,
  Audience,
  ProductCategory,
} from '../../models/product.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NgFor, RouterLink, ProductCardComponent, InrCurrencyPipe],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  private readonly productService = inject(ProductService);

  readonly featured = this.productService.getFeatured(8);
  readonly categoryLabels = CATEGORY_LABELS;
  readonly audienceLabels = AUDIENCE_LABELS;

  readonly categories: ProductCategory[] = ['spectacles', 'goggles', 'contact-lenses'];
  readonly audiences: Audience[] = ['women', 'men', 'kids'];
  readonly freeShippingMin = FREE_SHIPPING_THRESHOLD;
  readonly eyeExamMin = EYE_EXAM_MIN_ORDER;
}
