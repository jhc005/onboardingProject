import { Component } from '@angular/core';
import { AcessTokensFormsComponent } from '../../modules/acessTokens/infrastructure/components/acessTokens-forms.component';

@Component({
  selector: 'app-onboarding-page',
  imports: [AcessTokensFormsComponent],
  template: '<app-access-tokens-form />',
})
export class OnboardingPageComponent {}
