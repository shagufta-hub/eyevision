import { Component, inject } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { ProductService } from '../../services/product.service';
import {
  AUDIENCE_LABELS,
  Audience,
  CATEGORY_LABELS,
  ProductCategory,
} from '../../models/product.model';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NgFor, NgIf, RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  readonly cart = inject(CartService);
  readonly saleCount = inject(ProductService).getOnSale().length;
  menuOpen = false;

  readonly categories: { key: ProductCategory; label: string }[] = [
    { key: 'spectacles', label: CATEGORY_LABELS.spectacles },
    { key: 'goggles', label: CATEGORY_LABELS.goggles },
    { key: 'contact-lenses', label: CATEGORY_LABELS['contact-lenses'] },
  ];

  readonly audiences: { key: Audience; label: string }[] = [
    { key: 'women', label: AUDIENCE_LABELS.women },
    { key: 'men', label: AUDIENCE_LABELS.men },
    { key: 'kids', label: AUDIENCE_LABELS.kids },
    // { key: 'addProduct', label: AUDIENCE_LABELS.addProduct },
  ];

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  closeMenu(): void {
    this.menuOpen = false;
  }
}
