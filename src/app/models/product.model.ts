export type ProductCategory = 'spectacles' | 'goggles' | 'contact-lenses';
export type Audience = 'women' | 'men' | 'kids' ;

export interface ColorVariant {
  name: string;
  hex: string;
  image: string;
 sku?: string;
}

export interface Product {
  title: string;
  id: string;
  name: string;
  description: string;
  /** Price in Indian Rupees (INR). */
  price: number;
  originalPrice?: number;
  basePrice?:number;
  category: ProductCategory;
  audience: Audience;
  brand: string;
  image: string;
  attachment?:string;
  variants: ColorVariant[];
  rating: number;
  reviewCount: number;
  inStock: boolean;
  featured?: boolean;
  colors:any;
  images?: ProductImage[];
}

export interface CartItem {
  name: string;
  product: Product;
  quantity: number;
  selectedColor: string;
  title: string;
  price: number;
  image: string;
}

export interface ProductFilters {
  category?: ProductCategory | 'all';
  audience?: Audience | 'all';
  search?: string;
  sort?: 'featured' | 'price-asc' | 'price-desc' | 'rating' | 'discount';
  onSale?: boolean;
}

export const CATEGORY_LABELS: Record<ProductCategory, string> = {
  spectacles: 'Spectacles',
  goggles: 'Goggles',
  'contact-lenses': 'Contact Lenses',
};

export const AUDIENCE_LABELS: Record<Audience, string> = {
  women: 'Women',
  men: 'Men',
  kids: 'Kids',
};

export interface ProductImage {
  id: number;
  fileName: string;
  filePath: string;
  fileType: string;
  isPrimary: boolean;
  sortOrder: number;
}
