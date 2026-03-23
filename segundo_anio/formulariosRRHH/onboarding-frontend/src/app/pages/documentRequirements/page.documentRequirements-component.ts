import { Component } from '@angular/core';
import { DocumentRequirementsListComponent } from '../../modules/documentRequirements/infrastructure/components/documentRequirements-list.component';

@Component({
  selector: 'app-document-requirements-page',
  imports: [DocumentRequirementsListComponent],
  template: '<app-document-requirements-list />',
})
export class DocumentRequirementsPageComponent {}
