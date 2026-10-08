import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as QRCode from 'qrcode';

@Component({
  selector: 'app-exhibition-qr',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exhibition-qr.component.html',
  styleUrls: ['./exhibition-qr.component.scss']
})
export class ExhibitionQrComponent {
      registrationUrl = `${window.location.origin}/exhibition`;

  ngOnInit(): void {
    this.generateQrCode();
  }

  async generateQrCode(): Promise<void> {
    const canvas = document.getElementById(
      'registrationQrCanvas'
    ) as HTMLCanvasElement | null;

    if (!canvas) {
      return;
    }

    try {
      await QRCode.toCanvas(
        canvas,
        this.registrationUrl,
        {
          width: 300,
          margin: 2,
          errorCorrectionLevel: 'M'
        }
      );
    } catch (error) {
      console.error('QR generation failed:', error);
    }
  }

  downloadQr(): void {
    const canvas = document.getElementById(
      'registrationQrCanvas'
    ) as HTMLCanvasElement | null;

    if (!canvas) {
      return;
    }

    const link = document.createElement('a');

    link.download = 'eyevision-exhibition-qr.png';
    link.href = canvas.toDataURL('image/png');

    link.click();
  }

  printQr(): void {
    window.print();
  }
}
