import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { CandidatesService } from '../../../application/candidates.service';
import { Candidate } from '../../../domain/candidates';
import { CandidatesFormsComponent } from '../forms/candidates-forms.component';

@Component({
  selector: 'app-candidates-list',
  imports: [RouterLink, DatePipe, CandidatesFormsComponent],
  templateUrl: './candidates-list.component.html',
})
export class CandidatesListComponent implements OnInit {
  private readonly svc = inject(CandidatesService);

  candidates = signal<Candidate[]>([]);
  loading = signal(false);
  showModal = signal(false);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.svc.findAll().subscribe({
      next: (data) => { this.candidates.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  onSaved(): void {
    this.showModal.set(false);
    this.load();
  }
}
