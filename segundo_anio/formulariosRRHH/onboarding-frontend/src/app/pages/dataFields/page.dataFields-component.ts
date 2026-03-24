import { Component } from '@angular/core';
import { DataFieldsListComponent } from '../../modules/dataFields/infrastructure/components/lists/dataFields-list.component';

@Component({
  selector: 'app-fields-page',
  imports: [DataFieldsListComponent],
  template: '<app-data-fields-list />',
})
export class FieldsPageComponent {}
