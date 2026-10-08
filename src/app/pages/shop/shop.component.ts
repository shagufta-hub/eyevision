import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../components/product-card/product-card.component';
import {
  AUDIENCE_LABELS,
  CATEGORY_LABELS,
  Audience,
  Product,
  ProductCategory,
  ProductFilters,
} from '../../models/product.model';
import { ShopService } from 'src/app/services/shop.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [NgFor, NgIf, FormsModule, RouterLink, ProductCardComponent],
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.scss'],
})
export class ShopComponent implements OnInit {
  private readonly shopService = inject(ShopService);
  private readonly route = inject(ActivatedRoute);
    private readonly productservice = inject(ProductService);
 loading=false
 allProducts: Product[] = [];
products: Product[] = [];
filters: ProductFilters = {
  category: 'all',
  audience: 'all',
  search: '',
  sort: 'featured',
};

  readonly categoryLabels = CATEGORY_LABELS;
  readonly audienceLabels = AUDIENCE_LABELS;

  readonly categories: (ProductCategory | 'all')[] = [
    'all',
    'spectacles',
    'goggles',
    'contact-lenses',
  ];
  readonly audiences: (Audience | 'all')[] = ['all', 'women', 'men', 'kids'];
// for shp
 ngOnInit(): void {

  console.log('ShopComponent initialized');

  // First listen to URL query parameters
  this.route.queryParams.subscribe((params) => {

    console.log('Query Params:', params);

    this.filters = {
      category:
        params['category'] || 'all',

      audience:
        params['audience'] || 'all',

      search:
        params['search'] || '',

      sort:
        params['sort'] || 'featured'
    };

    console.log('Filters from URL:', this.filters);

    // If products already loaded, apply immediately
    if (this.allProducts.length > 0) {
      this.applyFilters();
    }

  });

  // Load products from backend
  this.loadProducts();
}
loadProducts(): void {

  this.loading = true;

  this.shopService.getProducts().subscribe({

    next: (products) => {

      console.log('Products from backend:', products);

      // Keep original backend data
      this.allProducts = products ?? [];

      console.log(
        'All products count:',
        this.allProducts.length
      );

      // Apply URL filters
      this.applyFilters();

      this.loading = false;
    },

    error: (error) => {

      console.error(
        'Failed to load products:',
        error
      );

      this.allProducts = [];
      this.products = [];

      this.loading = false;
    }

  });
}
   loadProductspre(): void {

    this.loading = true;

    this.shopService.getProducts().subscribe({

      next: (products) => {

        console.log('Products from backend:', products);

        this.products = products;

        this.applyFilters();

        this.loading = false;
      },

      error: (error) => {

        console.error(
          'Failed to load products:',
          error
        );

        this.products = [];

        this.loading = false;
      }
    });
  }
clearFilters(): void {

  this.filters = {
    category: 'all',
    audience: 'all',
    search: '',
    sort: 'featured'
  };

  this.applyFilters();
}
applyFilters(): void {

  // ALWAYS start from original products
  let filteredProducts = [...this.allProducts];

  console.log('Starting products:', filteredProducts.length);
  console.log('Current filters:', this.filters);


  // ==========================================
  // SEARCH
  // ==========================================

  const search =
    this.filters.search?.trim().toLowerCase();

  if (search) {

    filteredProducts = filteredProducts.filter(product => {

      return (
        product.title?.toLowerCase().includes(search) ||
        product.brand?.toLowerCase().includes(search) ||
        product.category?.toLowerCase().includes(search) ||
        product.audience?.toLowerCase().includes(search)
      );

    });
  }


  // ==========================================
  // CATEGORY
  // ==========================================

  if (this.filters.category && this.filters.category !== 'all') {

    filteredProducts =
      filteredProducts.filter(product =>

        product.category?.toLowerCase() ===
        this.filters.category!.toLowerCase()

      );
  }


  // ==========================================
  // AUDIENCE
  // ==========================================

  if (this.filters.audience && this.filters.audience !== 'all') {

    filteredProducts =
      filteredProducts.filter(product =>

        product.audience?.toLowerCase() ===
        this.filters.audience!.toLowerCase()

      );
  }


  // ==========================================
  // SORT
  // ==========================================

  switch (this.filters.sort) {

    case 'price-asc':

      filteredProducts.sort(
        (a, b) =>
          Number(a.price || 0) -
          Number(b.price || 0)
      );

      break;


    case 'price-desc':

      filteredProducts.sort(
        (a, b) =>
          Number(b.price || 0) -
          Number(a.price || 0)
      );

      break;


    case 'rating':

      filteredProducts.sort(
        (a, b) =>
          Number(b.rating || 0) -
          Number(a.rating || 0)
      );

      break;


    case 'featured':

      filteredProducts.sort(
        (a, b) =>
          Number(b.featured) -
          Number(a.featured)
      );

      break;
  }


  // ==========================================
  // UPDATE UI
  // ==========================================

  this.products = filteredProducts;

  console.log(
    'Final filtered products:',
    this.products.length
  );
}


  getCategoryLabel(
    key: ProductCategory | 'all'
  ): string {

    return key === 'all'
      ? 'All Categories'
      : this.categoryLabels[key];
  }


  getAudienceLabel(
    key: Audience | 'all'
  ): string {

    return key === 'all'
      ? 'Everyone'
      : this.audienceLabels[key];
  }
}
