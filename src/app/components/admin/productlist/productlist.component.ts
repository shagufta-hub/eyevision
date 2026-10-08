import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-productlist',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './productlist.component.html',
  styleUrls: ['./productlist.component.scss']
})
export class ProductlistComponent {
  loading:boolean=false;
  products:any;
  constructor(private productservice:ProductService){
    this.loadProducts()
  }
 loadProducts(): void {

    this.loading = true;

    this.productservice.getAll().subscribe({

      next: (products) => {
this.products = products;
        console.log('Products from backend:', products);
        this.loading = false;
      },

      error: (error) => {

        console.error(
          'Failed to load products:',
          error
        );

        this.products =[]

        this.loading = false;
      }
    });
  }
  Publishproduct(productid:number){
    console.log(productid);
    this.productservice.updateproductstatus(productid).subscribe(res=>{
      console.log(res);
      
    })
    
  }
}
