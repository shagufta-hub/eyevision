import { CurrencyPipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';
import { CURRENCY_CODE } from '../constants/currency.constants';

@Pipe({
  name: 'inr',
  standalone: true,
})
export class InrCurrencyPipe implements PipeTransform {
  private readonly currencyPipe = new CurrencyPipe('en-IN');

  transform(value: number | null | undefined): string {
    if (value == null) {
      return '';
    }
    return (
      this.currencyPipe.transform(value, CURRENCY_CODE, 'symbol', '1.0-0') ?? ''
    );
  }
}
