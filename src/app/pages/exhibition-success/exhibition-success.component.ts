import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExhibitionRegistrationResponse } from 'src/app/models/Exhibition.model';
import { Router } from '@angular/router';
import * as QRCode from 'qrcode';
import { ExhibitionService } from 'src/app/services/exhibition.service';


@Component({
  selector: 'app-exhibition-success',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exhibition-success.component.html',
  styleUrls: ['./exhibition-success.component.scss']
})
export class ExhibitionSuccessComponent {
  constructor(
  private exhibitionService: ExhibitionService,
  private router: Router
) {}
   registration?: ExhibitionRegistrationResponse; 
   qrData = '';
  ngOnInit(): void { 
  
      
      /* * Browser refresh hone par * navigation state lost ho sakta hai. * * Isliye localStorage se registration * recover kar rahe hain. */ 
      const saved = localStorage.getItem('exhibitionRegistration');
       if (saved) { try { this.registration = JSON.parse(saved); } 
       catch (error) { console.error('Unable to read saved registration:', error);
         localStorage.removeItem('exhibitionRegistration'); } }  /* * Agar registration nahi mila, * registration page par wapas bhej do. */
          if (!this.registration) { this.router.navigate(['/exhibition']); return; } /* * Membership QR URL. * * Example: * http://localhost:4200/exhibition/verify/EV26-A83F91C2 * * Production mein window.location.origin * automatically actual domain hoga. */ 
          this.qrData = `${window.location.origin}/exhibition/verify/${encodeURIComponent(this.registration.membershipToken)}`; /* * Browser refresh ke liye registration save. */
           localStorage.setItem('exhibitionRegistration', JSON.stringify(this.registration));
           } 
           ngAfterViewInit(): void {
             /* * QR canvas tab generate hoga jab * HTML render ho chuka hoga. */ setTimeout(() => { this.generateQrCode(); }); } 
             
             async generateQrCode(): Promise<void> { if (!this.qrData) { return; }
              const canvas = document.getElementById('membershipQrCanvas') as HTMLCanvasElement | null; 
              if (!canvas) { console.error('Membership QR canvas not found.'); return; } 
              try { await QRCode.toCanvas(canvas, this.qrData, { width: 220, margin: 2, errorCorrectionLevel: 'M' }); } 
              catch (error) { console.error('Membership QR generation failed:', error); } } 
           goToRegistration(): void { localStorage.removeItem('exhibitionRegistration'); this.router.navigate(['/exhibition']); } printMembership(): void { window.print(); }

}
