import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { OrdertrackingserviceService } from './ordertrackingservice.service';

@Component({
  selector: 'app-ordertracking',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ordertracking.component.html',
  styleUrls: ['./ordertracking.component.scss']
})
export class OrdertrackingComponent {
  Orderdetails: any;
  steps = [
    { name: 'Order Placed', completed: false },
    { name: 'Confirmed', completed: false },
    { name: 'Packed', completed: false },
    { name: 'Shipped', completed: false },
    { name: 'Out For Delivery', completed: false },
    { name: 'Delivered', completed: false }
  ];
  constructor(private router: Router, private activatedRoute: ActivatedRoute,private ordertrackingserviceService: OrdertrackingserviceService  ) { 
    console.log('OrdertrackingComponent initialized',);
    this.activatedRoute.queryParams.subscribe(prams=>{
      let orderId = prams['orderId'];
      console.log('queryParams order id',orderId);
      this.ordertrackingserviceService.getOrders_statusbyid(orderId).subscribe(orderStatus => {
        console.log('Order status', orderStatus);
        this.Orderdetails = orderStatus;
        if(this.Orderdetails.status === 'CONFIRMED'){
          this.steps[0].completed = true;
          this.steps[1].completed = true;
        } else if(this.Orderdetails.status === 'PACKED'){
          this.steps[0].completed = true;
          this.steps[1].completed = true;
          this.steps[2].completed = true;
        } else if(this.Orderdetails.status === 'SHIPPED'){
          this.steps[0].completed = true;
          this.steps[1].completed = true;
          this.steps[2].completed = true;
          this.steps[3].completed = true;
        } else if(this.Orderdetails.status === 'OUT_FOR_DELIVERY'){
          this.steps[0].completed = true;
          this.steps[1].completed = true;
          this.steps[2].completed = true;
          this.steps[3].completed = true;
          this.steps[4].completed = true;
        } else if(this.Orderdetails.status === 'DELIVERED'){
          this.steps[0].completed = true;
          this.steps[1].completed = true;
          this.steps[2].completed = true;
          this.steps[3].completed = true;
          this.steps[4].completed = true;
          this.steps[5].completed = true;
        }
      });
    });
  }
    order = {
    orderId: 'ORD123456',
    trackingNumber: 'TRK987654321',
    productName: 'Wireless Headphones',
    estimatedDelivery: '20 June 2026',
    status: 'Shipped',
    address: 'Ghaziabad, Uttar Pradesh, India'
  };
  

}
