import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { Product } from '../models/product.model';
import { API_CONFIG } from '../api.config';

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  // private baseUrl = 'http://localhost:8080/api';
  private baseUrl = API_CONFIG.baseUrl;
  private readonly imageBaseUrl = 'https://evoptical.in';


  constructor(private http: HttpClient) {}

  getproductdetails(id: any): Observable<Product> {
    return this.http.get<any>(`${this.baseUrl}/shop/productsbyid/${id}`).pipe(
      map((item) => this.mapBackendProduct(item))
    );
  }

  getProducts(): Observable<Product[]> {

      return this.http
      .get<any[]>(`${this.baseUrl}/shop/products`)
      .pipe(
        map((products:any[]) =>
          products.map((item:any) =>
            this.mapBackendProduct(item)
          )
        )
      );
  }
  

  // Existing methods...
private mapBackendProduct(item: any): Product {

  // ---------------------------------------
  // Product attachments / multiple images
  // ---------------------------------------

  const images = (item.attachments ?? [])
    .sort(
      (a: any, b: any) =>
        Number(a.sortOrder ?? 0) -
        Number(b.sortOrder ?? 0)
    )
    .map((attachment: any) => ({
      id: Number(attachment.id),
      fileName: attachment.fileName ?? '',
      filePath: attachment.filePath ?? '',
      fileType: attachment.fileType ?? '',
      isPrimary: attachment.isPrimary === true,
      sortOrder: Number(attachment.sortOrder ?? 0)
    }));


  // ---------------------------------------
  // Find primary image
  // ---------------------------------------

  const primaryImage =
    images.find((image: any) => image.isPrimary) ??
    images[0];


  // ---------------------------------------
  // Main product image
  // ---------------------------------------

  let image =
    'assets/images/product-placeholder.png';

  if (primaryImage?.filePath) {

    image =
      `${this.imageBaseUrl}${primaryImage.filePath}`;

  } else if (item.attachment) {

    // Old/single image fallback

    image =
      `${this.imageBaseUrl}${item.attachment}`;
  }


  console.log(
    'Mapped product images:',
    images
  );

  console.log(
    'Primary product image:',
    image
  );


  // ---------------------------------------
  // Variants
  // ---------------------------------------

  const variants =
    item.variants ?? [];


  // ---------------------------------------
  // Return Product
  // ---------------------------------------

  return {

    name: item.name ?? '',

    id: String(item.id),

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
      item.category || 'spectacles',

    audience:
      item.audience || 'all',

    brand:
      item.brand || 'EyeVision',


    // ---------------------------------------
    // Primary image
    // ---------------------------------------

    image: image,


    // ---------------------------------------
    // Multiple product images
    // ---------------------------------------

    images: images,


    // ---------------------------------------
    // Colors
    // ---------------------------------------

    colors: variants
      .map((variant: any) => variant.color)
      .filter(Boolean),


    // ---------------------------------------
    // Rating
    // ---------------------------------------

    rating:
      item.rating != null
        ? Number(item.rating)
        : 0,


    // ---------------------------------------
    // Reviews
    // ---------------------------------------

    reviewCount:
      item.reviewCount != null
        ? Number(item.reviewCount)
        : 0,


    // ---------------------------------------
    // Stock
    // ---------------------------------------

    inStock:
      variants.length === 0
        ? true
        : variants.some(
            (variant: any) =>
              Number(variant.stock ?? 0) > 0
          ),


    // ---------------------------------------
    // Featured
    // ---------------------------------------

    featured:
      item.featured === true ||
      item.featured === 'true',


    // ---------------------------------------
    // Variants
    // ---------------------------------------

    variants: variants
  };
}
}