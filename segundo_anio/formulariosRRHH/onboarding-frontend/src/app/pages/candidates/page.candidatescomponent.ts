import { Component } from '@angular/core';
import { CandidatesListComponent } from '../../modules/candidates/infrastructure/components/lists/candidates-list.component';

@Component({
  selector: 'app-candidates-page',
  imports: [CandidatesListComponent],
  template: '<app-candidates-list />',
})
export class CandidatesPageComponent {}
