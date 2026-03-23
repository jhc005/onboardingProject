import { Component, Input } from '@angular/core';
import { OnboardingProcessesFormsComponent } from '../../modules/onboardingProcesses/infrastructure/components/forms/onboardingProcesses-forms.component';

@Component({
  selector: 'app-process-detail-page',
  imports: [OnboardingProcessesFormsComponent],
  template: '<app-onboarding-processes-form [id]="id" />',
})
export class ProcessDetailPageComponent {
  @Input() id!: string;
}
