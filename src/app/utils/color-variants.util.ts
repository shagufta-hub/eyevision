import { ColorVariant, Product } from '../models/product.model';

const COLOR_HEX: Record<string, string> = {
  Tortoise: '#8B5A2B',
  Black: '#1a1a1a',
  'Rose Gold': '#B76E79',
  Gold: '#D4AF37',
  Silver: '#C0C0C0',
  Crystal: '#E8E8E8',
  Gunmetal: '#536267',
  'Matte Black': '#2d2d2d',
  Navy: '#1e3a5f',
  Brown: '#6B4423',
  Havana: '#8B4513',
  'Black/Gold': '#1a1a1a',
  Blue: '#2563eb',
  Pink: '#ec4899',
  Green: '#16a34a',
  Red: '#dc2626',
  Purple: '#9333ea',
  Yellow: '#eab308',
  'Orange/Black': '#ea580c',
  'Navy/White': '#1e3a5f',
  'White/Pink': '#fce7f3',
  'Black/Orange': '#1a1a1a',
  Stealth: '#374151',
  White: '#f5f5f5',
  Clear: '#dbeafe',
  Smoke: '#6b7280',
  'Mirror Blue': '#3b82f6',
  'Blue Frame': '#2563eb',
  Shark: '#64748b',
  Unicorn: '#c084fc',
  Dinosaur: '#22c55e',
  Rainbow: '#ec4899',
  'Blue Monster': '#3b82f6',
  Honey: '#d97706',
  Gray: '#9ca3af',
};

const DEFAULT_HEX = '#0d6e6e';

export function getColorHex(name: string): string {
  return COLOR_HEX[name] ?? DEFAULT_HEX;
}

export function createColorVariants(
  names: string[],
  defaultImage: string,
  alternateImages?: string[]
): ColorVariant[] {
  return names.map((name, index) => ({
    name,
    hex: getColorHex(name),
    image: alternateImages?.[index] ?? defaultImage,
  }));
}

export function getVariantByName(
  product: Product,
  colorName: string
): ColorVariant | undefined {
  return product.variants.find((v) => v.name === colorName);
}

export function getVariantImage(product: Product, colorName: string): string {
  return getVariantByName(product, colorName)?.image ?? product.image;
}

export function cartLineId(productId: string, color: string): string {
  return `${productId}::${color}`;
}
