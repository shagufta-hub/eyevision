import { registerLocaleData } from '@angular/common';
import localeEnIn from '@angular/common/locales/en-IN';
import { ApplicationConfig, LOCALE_ID } from '@angular/core';
import { provideRouter, withInMemoryScrolling } from '@angular/router';
import { provideHttpClient, withXsrfConfiguration } from '@angular/common/http';
import { routes } from './app.routes';

registerLocaleData(localeEnIn);

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: LOCALE_ID, useValue: 'en-IN' },
    provideRouter(
      routes,
      withInMemoryScrolling({ scrollPositionRestoration: 'top' })
    ),
    provideHttpClient(
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      })
    ),
  ],
};
