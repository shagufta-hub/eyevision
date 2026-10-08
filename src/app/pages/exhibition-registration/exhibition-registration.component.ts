import { Component, inject } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ExhibitionService } from 'src/app/services/exhibition.service';
import { Router, RouterLink } from '@angular/router';
import { InrCurrencyPipe } from 'src/app/pipes/inr-currency.pipe';

@Component({
  selector: 'app-exhibition-registration',
  standalone: true,
  imports: [CommonModule,NgIf, NgFor, FormsModule, InrCurrencyPipe, RouterLink,ReactiveFormsModule],
  templateUrl: './exhibition-registration.component.html',
  styleUrls: ['./exhibition-registration.component.scss']
})
export class ExhibitionRegistrationComponent {
    private readonly fb = inject(FormBuilder);
  private readonly exhibitionService = inject(ExhibitionService);
  private readonly router = inject(Router);

  loading = false;
  submitted = false;
  errorMessage = '';

  /*
   * IMPORTANT
   * Ye wahi exhibition ID hai jo backend
   * database mein create hui thi.
   *
   * Example:
   * POST /api/admin/exhibitions
   * response id = 1
   */
  exhibitionId = 1;

  registrationForm = this.fb.group({
    name: [
      '',
      [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(100)
      ]
    ],

    mobile: [
      '',
      [
        Validators.required,
        Validators.pattern(/^[6-9][0-9]{9}$/)
      ]
    ],

    email: [
      '',
      [
        Validators.email
      ]
    ]
  });

  get name() {
    return this.registrationForm.get('name');
  }

  get mobile() {
    return this.registrationForm.get('mobile');
  }

  get email() {
    return this.registrationForm.get('email');
  }

  submit(): void {

    this.submitted = true;
    this.errorMessage = '';

    if (this.registrationForm.invalid) {

      this.registrationForm.markAllAsTouched();

      return;
    }

    this.loading = true;

    const formValue =
      this.registrationForm.getRawValue();

    const request = {
      name: formValue.name!.trim(),
      mobile: formValue.mobile!.trim(),
      email: formValue.email?.trim() || '',
      exhibitionId: this.exhibitionId
    };

    this.exhibitionService
      .registerCustomer(request)
      .subscribe({

        next: (response) => {

          this.loading = false;
          if (!response.success) {

            this.errorMessage =
              response.message ||
              'Registration failed';

            return;
          }

          /*
           * Token generated successfully.
           *
           * Navigate to success page.
           */
   localStorage.setItem(
        'exhibitionRegistration',
        JSON.stringify(response)
      );
          this.router.navigate(
            ['/exhibition/success']
          );
        },

        error: (error) => {

          this.loading = false;

          console.error(
            'Exhibition registration error:',
            error
          );

          this.errorMessage =
            error?.error?.message ||
            'Something went wrong. Please try again.';
        }
      });
  }

  getexhibitionDetails(id:any) {
    this.exhibitionService.getExhibitionDetails(id)
      .subscribe({
        next: (response:any) => {
          console.log('Exhibition details:', response);
        },
        error: (error:Error) => {
          console.error('Error fetching exhibition details:', error);
        }
      });
    }

}
