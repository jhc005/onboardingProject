import { Component, Input } from '@angular/core';
import { DataAnswer } from '../../../domain/dataAnswers';

@Component({
  selector: 'app-data-answers-list',
  imports: [],
  templateUrl: './dataAnswers-list.component.html',
  styleUrl: './dataAnswers-list.component.css',
})
export class DataAnswersListComponent {
  @Input() answers: DataAnswer[] = [];
}
