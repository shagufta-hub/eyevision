// src/app/admin-orders.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from './services/admin.service';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-orders.component.html',
  styleUrls: ['./admin-orders.component.css']
})
export class AdminOrdersComponent implements OnInit {
  orders: any[] = [];
  loading = false;
  error = '';
  selectedOrder: any = null;
  showTrackingDetails = false;

  statuses = ['PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

  statusColors: any = {
    'PENDING': '#FFC107',
    'CONFIRMED': '#4CAF50',
    'PROCESSING': '#2196F3',
    'SHIPPED': '#FF9800',
    'DELIVERED': '#4CAF50',
    'CANCELLED': '#F44336'
  };

  statusEmojis: any = {
    'PENDING': '⏳',
    'CONFIRMED': '✅',
    'PROCESSING': '🔄',
    'SHIPPED': '📦',
    'DELIVERED': '✨',
    'CANCELLED': '❌'
  };

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.loadAllOrders();
  }

  loadAllOrders() {
    this.loading = true;
    this.adminService.getAllOrders().subscribe(
      (data) => {
        this.orders = data;
        this.loading = false;
      },
      (error) => {
        this.error = 'Failed to load orders';
        this.loading = false;
      }
    );
  }

  updateStatus(orderId: number, newStatus: string) {
    if (confirm(`Update order #${orderId} to ${newStatus}?`)) {
      this.adminService.updateOrderStatus(orderId, newStatus).subscribe(
        () => {
          alert(`✅ Order updated to ${newStatus}`);
          this.loadAllOrders();
        },
        () => alert('❌ Failed to update order')
      );
    }
  }

  viewTracking(order: any) {
    this.selectedOrder = order;
    this.adminService.getTrackingHistory(order.orderId).subscribe(
      (data) => {
        this.selectedOrder.trackingHistory = data;
        this.showTrackingDetails = true;
      }
    );
  }

  closeModal() {
    this.showTrackingDetails = false;
    this.selectedOrder = null;
  }

  getStatusEmoji(status: string): string {
    return this.statusEmojis[status] || '📍';
  }

  getStatusColor(status: string): string {
    return this.statusColors[status] || '#999';
  }
}
