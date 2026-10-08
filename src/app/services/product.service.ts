import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import {
  Audience,
  Product,
  ProductCategory,
  ProductFilters,
} from '../models/product.model';

import { API_CONFIG } from '../api.config';

@Injectable({
  providedIn: 'root',
})
export class ProductService {

  private readonly baseUrl = `${API_CONFIG.baseUrl}/admin`;

  private products: Product[] = [];

  constructor(private http: HttpClient) {}

  // =========================================================
  // GET PRODUCTS FROM BACKEND
  // =========================================================

  getAll(): Observable<Product[]> {

    return this.http
      .get<any[]>(`${this.baseUrl}/getproduclistt`)
      .pipe(
        map((response) => {

          const products = response.map((item) =>
            this.mapBackendProduct(item)
          );

          this.products = products;

          return products;
        })
      );
  }


  // =========================================================
  // GET SINGLE PRODUCT
  // =========================================================

  getById(id: string): Product | undefined {

    return this.products.find(
      (product) => String(product.id) === String(id)
    );
  }


  // =========================================================
  // FEATURED PRODUCTS
  // =========================================================

  getFeatured(limit = 8): Product[] {

    return this.products
      .filter((product) => product.featured)
      .slice(0, limit);
  }


  // =========================================================
  // SALE PRODUCTS
  // =========================================================

  getOnSale(): Product[] {

    return this.products.filter(
      (product) => this.isOnSale(product)
    );
  }


  // =========================================================
  // CHECK SALE
  // =========================================================

  isOnSale(product: Product): boolean {

    return (
      product.originalPrice != null &&
      product.originalPrice > product.price
    );
  }


  // =========================================================
  // FILTER PRODUCTS
  // =========================================================

  filter(filters: ProductFilters): Product[] {

    let result = [...this.products];


    // Category
    if (
      filters.category &&
      filters.category !== 'all'
    ) {
      result = result.filter(
        (product) =>
          product.category === filters.category
      );
    }


    // Audience
    if (
      filters.audience &&
      filters.audience !== 'all'
    ) {
      result = result.filter(
        (product) =>
          product.audience === filters.audience
      );
    }


    // On Sale
    if (filters.onSale) {

      result = result.filter(
        (product) => this.isOnSale(product)
      );
    }


    // Search
    if (filters.search?.trim()) {

      const q = filters.search
        .trim()
        .toLowerCase();

      result = result.filter(
        (product) =>
          product.name.toLowerCase().includes(q) ||
          product.description.toLowerCase().includes(q) ||
          product.brand.toLowerCase().includes(q)
      );
    }


    // Sort
    switch (filters.sort) {

      case 'price-asc':

        result.sort(
          (a, b) => a.price - b.price
        );

        break;


      case 'price-desc':

        result.sort(
          (a, b) => b.price - a.price
        );

        break;


      case 'rating':

        result.sort(
          (a, b) => b.rating - a.rating
        );

        break;


      case 'discount':

        result.sort(
          (a, b) =>
            (1 - b.price / (b.originalPrice ?? b.price)) -
            (1 - a.price / (a.originalPrice ?? a.price))
        );

        break;


      default:

        result.sort(
          (a, b) =>
            Number(b.featured) -
            Number(a.featured)
        );

        break;
    }


    return result;
  }


  // =========================================================
  // CATEGORY + AUDIENCE
  // =========================================================

  getByCategoryAndAudience(
    category: ProductCategory,
    audience: Audience
  ): Product[] {

    return this.filter({
      category,
      audience,
      search: '',
      sort: 'featured',
    });
  }


  // =========================================================
  // MAP BACKEND PRODUCT -> FRONTEND PRODUCT
  // =========================================================

private mapBackendProduct(item: any): Product {

  const variants = item.variants ?? [];
  

  // Get all variant attachments
  const attachments = variants.flatMap(
    (variant: any) => variant.attachments ?? []
  );

  // Primary image
  const primaryAttachment =
    attachments.find(
      (attachment: any) => attachment.isPrimary === true
    ) ?? attachments[0];

  const image =
  primaryAttachment?.filePath
    ? `https://evoptical.in${primaryAttachment.filePath}`
    : item.attachment
      ? `https://evoptical.in${item.attachment}`
      : 'assets/images/product-placeholder.png';

  // Colors
  const colors = variants
    .map((variant: any) => variant.color)
    .filter(
      (color: string | null | undefined) => !!color
    );

  return {
    name: item.name ?? '',

    id: String(item.id),

    // IMPORTANT: your Product model expects title
    title: item.title ?? '',

    description: item.description ?? '',

    price:
      item.basePrice != null
        ? Number(item.basePrice)
        : 0,

    originalPrice:
      item.originalPrice != null
        ? Number(item.originalPrice)
        : undefined,

    category:
      item.category as ProductCategory,

    audience:
      item.audience as Audience,

    brand:
      item.brand ?? 'EyeVision',

    image,

    colors,

    rating:
      item.rating != null
        ? Number(item.rating)
        : 0,

    reviewCount:
      item.reviewCount != null
        ? Number(item.reviewCount)
        : 0,

    inStock:
      variants.some(
        (variant: any) =>
          Number(variant.stock ?? 0) > 0
      ),

    featured:
      item.featured === true ||
      item.featured === 'true',

    variants: variants,

  };
}


  // =========================================================
  // ADMIN - ADD PRODUCT
  // =========================================================

  addProduct(formData: FormData): Observable<any> {

    const headers = new HttpHeaders({
      Accept: 'application/json',
    });

    return this.http.post<any>(
      `${this.baseUrl}/addproduct`,
      formData,
      {
        headers,
      }
    );
  }


  // =========================================================
  // ADMIN - GET PRODUCT LIST
  // =========================================================

  getproductlist(): Observable<any[]> {

    return this.http.get<any[]>(
      `${this.baseUrl}/getproduclistt`
    );
  }


  // =========================================================
  // VARIANT ATTACHMENT
  // =========================================================

  uploadVariantAttachment(
    variantId: number,
    file: File,
    isPrimary: boolean = false
  ): Observable<any> {

    const formData = new FormData();

    formData.append(
      'file',
      file,
      file.name
    );

    formData.append(
      'isPrimary',
      String(isPrimary)
    );

    return this.http.post<any>(
      `${this.baseUrl}/variant/${variantId}/attachments`,
      formData,
      {
        headers: new HttpHeaders({
          Accept: 'application/json',
        }),
      }
    );
  }

  updateproductstatus(id:number): Observable<any>{
      return this.http.put<any>(`${this.baseUrl}/modifyproductstatus`,id );
  }
}