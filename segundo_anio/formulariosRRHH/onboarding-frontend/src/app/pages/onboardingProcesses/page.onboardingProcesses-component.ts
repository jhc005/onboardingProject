import { Component } from '@angular/core';
import { OnboardingProcessesListComponent } from '../../modules/onboardingProcesses/infrastructure/components/lists/onboardingProcesses-list.component';

@Component({
  selector: 'app-processes-page',
  imports: [OnboardingProcessesListComponent],
  template: '<app-onboarding-processes-list />',
})
export class ProcessesPageComponent {}
