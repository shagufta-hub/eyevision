import { Routes } from '@angular/router';
import { ExhibitionRegistrationComponent } from './pages/exhibition-registration/exhibition-registration.component';
import { ExhibitionSuccessComponent } from './pages/exhibition-success/exhibition-success.component';
import { ExhibitionQrComponent } from './pages/exhibition-qr/exhibition-qr.component';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/home/home.component').then((m) => m.HomeComponent),
  },
  {
    path: 'shop',
    loadComponent: () =>
      import('./pages/shop/shop.component').then((m) => m.ShopComponent),
  },
  {
    path: 'track',
    loadComponent: () =>
      import('./pages/ordertracking/ordertracking.component').then((m) => m.OrdertrackingComponent),
  },
  {
    path: 'sale',
    loadComponent: () =>
      import('./pages/sale/sale.component').then((m) => m.SaleComponent),
  },
  {
    path: 'product/:id',
    loadComponent: () =>
      import('./pages/product-detail/product-detail.component').then(
        (m) => m.ProductDetailComponent
      ),
  },
  {
    path: 'cart',
    loadComponent: () =>
      import('./pages/cart/cart.component').then((m) => m.CartComponent),
  },
  {
    path: 'checkout',
    loadComponent: () =>
      import('./pages/checkout/checkout.component').then(
        (m) => m.CheckoutComponent
      ),
  },
  {
    path: 'admin',
    loadComponent: () =>
      import('./admin-orders.component').then((m) => m.AdminOrdersComponent),
  },
   {
    path: 'admin/productlist',
    loadComponent: () =>
      import('./components/admin/productlist/productlist.component').then((m) => m.ProductlistComponent),
  },
   {
    path: 'admin/addproduct',
    loadComponent: () =>
      import('./components/admin/addproduct/addproduct.component').then((m) => m.AddproductComponent),
  },
    {
    path: 'exhibition',
    component: ExhibitionRegistrationComponent
  },

  {
    path: 'exhibition/success',
    component: ExhibitionSuccessComponent
  },
  {
    path: 'exhibition/qr',
    component: ExhibitionQrComponent
  },
  { path: '**', redirectTo: '' },
];
