import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { FieldOptionDraft } from '../../domain/fieldOptions';

@Component({
  selector: 'app-field-options-form',
  templateUrl: './fieldOptions-forms.component.html',
  styleUrl: './fieldOptions-forms.component.css',
})
export class FieldOptionsFormsComponent implements OnInit {
  @Input() options: FieldOptionDraft[] = [];
  @Output() optionsChange = new EventEmitter<FieldOptionDraft[]>();

  drafts = signal<FieldOptionDraft[]>([]);

  ngOnInit(): void {
    this.drafts.set([...this.options]);
  }

  add(): void {
    this.drafts.update(d => [...d, { optionLabel: '', optionValue: '' }]);
    this.optionsChange.emit(this.drafts());
  }

  remove(index: number): void {
    this.drafts.update(d => d.filter((_, i) => i !== index));
    this.optionsChange.emit(this.drafts());
  }

  update(index: number, field: 'optionLabel' | 'optionValue', value: string): void {
    this.drafts.update(d => {
      const copy = [...d];
      copy[index] = { ...copy[index], [field]: value };
      return copy;
    });
    this.optionsChange.emit(this.drafts());
  }
}
