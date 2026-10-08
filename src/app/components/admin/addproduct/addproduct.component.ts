import { Component,OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ProductService } from 'src/app/services/product.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-addproduct',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './addproduct.component.html',
  styleUrls: ['./addproduct.component.scss']
})
export class AddproductComponent implements OnInit {
   productForm!: FormGroup;
  selectedFiles: File[] = [];
primaryImageIndex = 0;
  variantFiles: any;

  constructor(private fb: FormBuilder,private addservice  : ProductService) {}
   audienceOptions = ['women', 'men', 'kids'];
  categoryOptions = ['spectacles', 'goggles', 'contact-lenses'];
  ngOnInit(): void {
    this.getProducts()
    this.productForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      basePrice: [0, Validators.required],
      status: ['active'],
      variants: this.fb.array([]) ,
      audience: [''],
      category:[''],
      brand: [''],
      originalPrice: [0],
      
      // Array to hold dynamically generated variant combinations
    });
  }

  get variants(): FormArray {
    return this.productForm.get('variants') as FormArray;
  }

  // Manually add an empty variant row to the form layout
  addVariantRow(): void {
    const variantGroup = this.fb.group({
      sku: ['', Validators.required],
      price: [0, Validators.required],
      stock: [0, Validators.required],
      color: [''], // Attribute 1
      size: ['']   // Attribute 2
    });
    this.variants.push(variantGroup);
  }

  removeVariantRow(index: number): void {
    this.variants.removeAt(index);
  }

  // onSubmit(): void {
  //   if (this.productForm.invalid) return;

  //   this.http.post('http://localhost:8080/api/admin/addproduct', this.productForm.value)
  //     .subscribe({
  //       next: () => alert('Product with variants saved!'),
  //       error: (err) => console.error(err)
  //     });
  // }
    isSubmitting = false;
    onSubmit(): void {

  if (this.productForm.invalid) {
    this.productForm.markAllAsTouched();
    return;
  }

  this.isSubmitting = true;

  const productData = {
    title: this.productForm.value.title,
    basePrice: this.productForm.value.basePrice,
    audience: this.productForm.value.audience,
    category: this.productForm.value.category,
    brand: this.productForm.value.brand,
    originalPrice: this.productForm.value.originalPrice,
    status: this.productForm.value.status,
    featured: this.productForm.value.featured,
    description: this.productForm.value.description,
    variants: this.productForm.value.variants
  };

  // Convert product JSON into Blob
  const productBlob = new Blob(
    [JSON.stringify(productData)],
    {
      type: 'application/json'
    }
  );

  // Create FormData
  const formData = new FormData();

  // Add product JSON
  formData.append(
    'product',
    productBlob
  );

  // Add multiple product images
  this.selectedFiles.forEach((file: File) => {

    formData.append(
      'files',
      file,
      file.name
    );

  });

  // Call API
  this.addservice
    .addProduct(formData)
    .subscribe({
      next: (response:any) => {

        console.log(
          'Product created successfully:',
          response
        );
   Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'Product saved successfully!',
            confirmButtonText: 'OK',
          }).then(() => {
            // Optional navigation or reset logic here
         


          this.isSubmitting = false;


          // Optional reset
          this.productForm.reset();


          this.variants.clear();

          this.addVariantRow();

          this.selectedFiles = [];
           });

        // success message / navigation
      },

      error: (error:Error) => {

        console.error(
          'Product creation failed:',
          error
        );

        this.isSubmitting = false;
      }
    });
}

onSubmitpre(): void {

  
    if (this.productForm.invalid) {

      this.productForm.markAllAsTouched();

      return;
    }


    this.isSubmitting = true;


    // =====================================================
    // CREATE FORMDATA
    // =====================================================

    const formData =
      new FormData();


    // =====================================================
    // PRODUCT JSON
    // =====================================================

    const productData =
      this.productForm.value;


    console.log(
      'Product JSON:',
      productData
    );


    const productBlob =
      new Blob(
        [
          JSON.stringify(
            productData
          )
        ],
        {
          type: 'application/json'
        }
      );


    formData.append(
      'product',
      productBlob
    );


    // =====================================================
    // ATTACHMENT
    // =====================================================
for(let i = 0; i < this.selectedFiles.length; i++) {
    if (this.selectedFiles[i]) {

      formData.append(
        'file',
        this.selectedFiles[i],
        this.selectedFiles[i].name
      );
    }
  }


    // =====================================================
    // CALL SERVICE
    // =====================================================

    this.addservice
      .addProduct(formData)
      .subscribe({

        next: (response) => {

          console.log(
            'Product saved:',
            response
          );
            const savedVariants = response.variants || [];

      console.log(
        'Saved variants:',
        savedVariants
      );

      // STEP 2: Upload variant attachments
      if(this.variantFiles.length > 0) {
        this.uploadAllVariantAttachments(savedVariants);
      }
      // this.uploadVariantImage(1, this.variantFiles[0],true);


          alert(
            'Product saved successfully!'
          );
          Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'Product saved successfully!',
            confirmButtonText: 'OK'
          }).then(() => {
            // Optional navigation or reset logic here
         


          this.isSubmitting = false;


          // Optional reset
          this.productForm.reset();


          this.variants.clear();

          this.addVariantRow();

          this.selectedFiles = [];
           });
          
        },


        error: (error) => {

          console.error(
            'Product save error:',
            error
          );


          this.isSubmitting = false;


          if (error.status === 415) {

            alert(
              'Unsupported Media Type. Please check multipart request.'
            );

          } else if (error.status === 413) {

            alert(
              'File size is too large.'
            );

          } else {

            alert(
              'Unable to save product.'
            );
          }
        }

      });

  // this.http.post(
  //   'http://localhost:8080/api/admin/addproduct',
  //   formData
  // ).subscribe({
  //   next: (response) => {
  //     console.log('Product saved:', response);
  //     alert('Product with attachment and variants saved successfully!');
  //   },
  //   error: (err) => {
  //     console.error('Product save failed:', err);
  //     alert('Unable to save product.');
  //   }
  // });
}
  getProducts(): void {4
    this.addservice.getproductlist().subscribe(
      (products) => {
        console.log('Product list:', products);
      },
      (err) => {
        console.error('Error fetching product list:', err);
      }
    );
    // this.http.get<any[]>('http://localhost:8080/api/admin/getproduclistt')
    //   .subscribe({
    //     next: (products) => console.log(products),
    //     error: (err) => console.error(err)
    //   });
  }
  onFileSelected(event: any): void {
  // const file = event.target.files?.[0];

  // if (!file) {
  //   this.selectedFiles = [];
  //   return;
  // }

  // const allowedTypes = [
  //   'image/jpeg',
  //   'image/png',
  //   'image/webp'
  // ];

  // if (!allowedTypes.includes(file.type)) {
  //   alert('Only JPG, PNG and WEBP files are allowed.');
  //   event.target.value = '';
  //   return;
  // }

  // const maxSize = 1 * 1024 * 1024;

  // if (file.size > maxSize) {
  //   alert('File size must not exceed 1 MB.');
  //   event.target.value = '';
  //   return;
  // }

  // this.selectedFiles.push(file);

    const input =
    event.target as HTMLInputElement;

  if (!input.files) {
    this.selectedFiles = [];
    return;
  }

  this.selectedFiles =
    Array.from(input.files);

  console.log(
    'Selected product images:',
    this.selectedFiles
  );

}


uploadVariantImage(
  variantId: number,
  file: File,
  isPrimary: boolean = false
): void {

  this.addservice
    .uploadVariantAttachment(
      variantId,
      file,
      isPrimary
    )
    .subscribe({
      next: (response) => {

        console.log(
          'Variant attachment uploaded:',
          response
        );

      },

      error: (error) => {

        console.error(
          'Variant attachment upload failed:',
          error
        );

      }
    });
}

uploadAllVariantAttachments(savedVariants: any[]): void {

  // Agar koi variant attachment nahi hai
  if (!savedVariants || savedVariants.length === 0) {

    this.isSubmitting = false;

    alert('Product saved successfully!');

    return;
  }

  let completed = 0;
  let totalUploads = 0;

  // Pehle total attachments count
  savedVariants.forEach((variant, index) => {

    const files = this.variantFiles[index] || [];

    totalUploads += files.length;
  });


  // Koi attachment nahi hai
  if (totalUploads === 0) {

    this.isSubmitting = false;

    alert('Product saved successfully!');

    return;
  }


  savedVariants.forEach((variant, index) => {

    const files = this.variantFiles[index] || [];

    files.forEach((file: File, fileIndex: number) => {

      const isPrimary = fileIndex === 0;

      this.addservice
        .uploadVariantAttachment(
          variant.id,
          file,
          isPrimary
        )
        .subscribe({

          next: (response) => {

            console.log(
              'Variant image uploaded:',
              response
            );

            completed++;

            if (completed === totalUploads) {

              this.isSubmitting = false;

              alert(
                'Product and variant images saved successfully!'
              );
            }
          },

          error: (error) => {

            console.error(
              'Variant image upload failed:',
              error
            );

            completed++;

            if (completed === totalUploads) {

              this.isSubmitting = false;

              alert(
                'Product saved, but some variant images could not be uploaded.'
              );
            }
          }
        });
    });
  });
}

onVariantFileSelected(
  event: any,
  variantIndex: number
): void {

  const files: FileList = event.target.files;

  if (!files || files.length === 0) {
    return;
  }

  this.variantFiles[variantIndex] = [];

  for (let i = 0; i < files.length; i++) {

    const file = files.item(i);

    if (!file) {
      continue;
    }

    const allowedTypes = [
      'image/jpeg',
      'image/png',
      'image/webp'
    ];

    if (!allowedTypes.includes(file.type)) {

      alert(
        'Only JPG, PNG and WEBP files are allowed.'
      );

      continue;
    }

    if (file.size > 1 * 1024 * 1024) {

      alert(
        `${file.name} must not exceed 1 MB.`
      );

      continue;
    }

    this.variantFiles[variantIndex].push(file);
  }
}

}
