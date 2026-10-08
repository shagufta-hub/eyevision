import { Product } from '../models/product.model';

export function getDiscountPercent(product: Product): number {
  if (!product.originalPrice || product.originalPrice <= product.price) {
    return 0;
  }
  return Math.round((1 - product.price / product.originalPrice) * 100);
}
