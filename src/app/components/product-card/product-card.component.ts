import { NgIf } from '@angular/common';
import { Component, Input, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Product, CATEGORY_LABELS, AUDIENCE_LABELS } from '../../models/product.model';
import { InrCurrencyPipe } from '../../pipes/inr-currency.pipe';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [RouterLink, InrCurrencyPipe, NgIf],
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss'],
})
export class ProductCardComponent {
  @Input({ required: true }) product!: Product;
  // productdata:Array<Product[]>=new Array<Product>
  // constructor(){
  //     console.log(this.product);
  //     let data=[Product]
  //     this.productdata=this.product
  //     data.map((e:any)=>{
  //         e.attachment='http://localhost:8080'+e.attachment
  //     })
  //     this.product=data
      

  // }

  private readonly cart = inject(CartService);

  readonly categoryLabels = CATEGORY_LABELS;
  readonly audienceLabels = AUDIENCE_LABELS;

  added = false;

  addToCart(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    const defaultColor = this.product.variants[0]?.name;
    this.cart.addToCart(this.product, 1, defaultColor);
    this.added = true;
    setTimeout(() => (this.added = false), 1500);
  }
}
