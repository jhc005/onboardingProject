import { Component, inject, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { AdminUsersService } from '../../../application/adminUsers.service';
import { AdminUser } from '../../../domain/adminUsers';
import { ToastService } from '../../../../../core/services/toast.service';
import { AdminUsersCreateFormComponent } from '../forms/create-form/adminUsers-create-form.component';

@Component({
  selector: 'app-admin-users-list',
  imports: [DatePipe, AdminUsersCreateFormComponent],
  templateUrl: './adminUsers-list.component.html',
})
export class AdminUsersListComponent implements OnInit {
  private readonly svc = inject(AdminUsersService);
  private readonly toast = inject(ToastService);

  admins = signal<AdminUser[]>([]);
  loading = signal(false);
  deleting = signal<number | null>(null);
  showModal = signal(false);
  confirmDeleteId = signal<number | null>(null);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.svc.findAll().subscribe({
      next: (data) => { this.admins.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  onSaved(): void {
    this.showModal.set(false);
    this.load();
  }

  confirmDelete(id: number): void {
    this.confirmDeleteId.set(id);
  }

  cancelDelete(): void {
    this.confirmDeleteId.set(null);
  }

  delete(id: number): void {
    this.deleting.set(id);
    this.svc.delete(id).subscribe({
      next: () => {
        this.toast.success('Administrador eliminado correctamente.');
        this.confirmDeleteId.set(null);
        this.deleting.set(null);
        this.load();
      },
      error: () => {
        this.deleting.set(null);
      },
    });
  }
}
