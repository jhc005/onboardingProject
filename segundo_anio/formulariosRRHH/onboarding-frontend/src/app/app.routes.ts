import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { candidateGuard } from './core/guards/candidate.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/adminUsers/page.adminUsers-component').then(
        (m) => m.LoginPageComponent
      ),
  },
  {
    path: 'admin',
    loadComponent: () =>
      import('./shared/components/admin-layout/admin-layout.component').then(
        (m) => m.AdminLayoutComponent
      ),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'candidates', pathMatch: 'full' },
      {
        path: 'candidates',
        loadComponent: () =>
          import('./pages/candidates/page.candidatescomponent').then(
            (m) => m.CandidatesPageComponent
          ),
      },
      {
        path: 'processes',
        loadComponent: () =>
          import(
            './pages/onboardingProcesses/page.onboardingProcesses-component'
          ).then((m) => m.ProcessesPageComponent),
      },
      {
        path: 'processes/:id',
        loadComponent: () =>
          import('./pages/processDetail/page.processDetail-component').then(
            (m) => m.ProcessDetailPageComponent
          ),
      },
      {
        path: 'fields',
        loadComponent: () =>
          import('./pages/dataFields/page.dataFields-component').then(
            (m) => m.FieldsPageComponent
          ),
      },
      {
        path: 'document-requirements',
        loadComponent: () =>
          import('./pages/documentRequirements/page.documentRequirements-component').then(
            (m) => m.DocumentRequirementsPageComponent
          ),
      },
      {
        path: 'users',
        loadComponent: () =>
          import('./pages/adminPanel/page.admin-users-component').then(
            (m) => m.AdminUsersPageComponent
          ),
      },
    ],
  },
  {
    path: 'onboarding',
    loadComponent: () =>
      import('./pages/accessTokens/page.accesTokens-component').then(
        (m) => m.OnboardingPageComponent
      ),
    canActivate: [candidateGuard],
  },
  { path: '**', redirectTo: 'login' },
];
