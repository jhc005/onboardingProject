import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataField } from '../../../../dataFields/domain/dataFields';

@Component({
  selector: 'app-data-answers-form',
  imports: [ReactiveFormsModule],
  templateUrl: './dataAnswers-forms.component.html',
  styleUrl: './dataAnswers-forms.component.css',
})
export class DataAnswersFormsComponent {
  @Input() fields: DataField[] = [];
  @Input() form!: FormGroup;

  getFieldControl(id: number) {
    return this.form?.get(`field_${id}`);
  }
}
