import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideToastr } from 'ngx-toastr';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { ADMIN_USERS_REPOSITORY } from './modules/adminUsers/infrastructure/config/adminUsers.token';
import { AdminUsersApi } from './modules/adminUsers/infrastructure/persistence/api/adminUsers.api';
import { CANDIDATES_REPOSITORY } from './modules/candidates/infrastructure/config/candidates.token';
import { CandidatesApi } from './modules/candidates/infrastructure/persistence/api/candidates.api';
import { ONBOARDING_PROCESSES_REPOSITORY } from './modules/onboardingProcesses/infrastructure/config/onboardingProcesses.token';
import { OnboardingProcessesApi } from './modules/onboardingProcesses/infrastructure/persistence/api/onboardingProcesses.api';
import { DATA_FIELDS_REPOSITORY } from './modules/dataFields/infrastructure/config/dataFields.token';
import { DataFieldsApi } from './modules/dataFields/infrastructure/persistence/api/dataFields.api';
import { ACESS_TOKENS_REPOSITORY } from './modules/acessTokens/infrastructure/config/acessTokens.token';
import { AccessTokensApi } from './modules/acessTokens/infrastructure/persistence/api/accessTokens.api';
import { DATA_ANSWERS_REPOSITORY } from './modules/dataAnswers/infrastructure/config/dataAnswers.token';
import { DataAnswersApi } from './modules/dataAnswers/infrastructure/persistence/api/dataAnswers.api';
import { FIELD_OPTIONS_REPOSITORY } from './modules/fieldOptions/infrastructure/config/fieldOptions.token';
import { FieldOptionsApi } from './modules/fieldOptions/infrastructure/persistence/api/fieldOptions.api';
import { DOCUMENT_REQUIREMENTS_REPOSITORY } from './modules/documentRequirements/infrastructure/config/documentRequirements.token';
import { DocumentRequirementsApi } from './modules/documentRequirements/infrastructure/persistence/api/documentRequirements.api';
import { UPLOADED_DOCUMENTS_REPOSITORY } from './modules/uploadedDocuments/infrastructure/config/uploadedDocuments.token';
import { UploadedDocumentsApi } from './modules/uploadedDocuments/infrastructure/persistence/api/uploadedDocuments.api';

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: ADMIN_USERS_REPOSITORY, useExisting: AdminUsersApi },
    { provide: CANDIDATES_REPOSITORY, useExisting: CandidatesApi },
    { provide: ONBOARDING_PROCESSES_REPOSITORY, useExisting: OnboardingProcessesApi },
    { provide: DATA_FIELDS_REPOSITORY, useExisting: DataFieldsApi },
    { provide: DATA_ANSWERS_REPOSITORY, useExisting: DataAnswersApi },
    { provide: ACESS_TOKENS_REPOSITORY, useExisting: AccessTokensApi },
    { provide: FIELD_OPTIONS_REPOSITORY, useExisting: FieldOptionsApi },
    { provide: DOCUMENT_REQUIREMENTS_REPOSITORY, useExisting: DocumentRequirementsApi },
    { provide: UPLOADED_DOCUMENTS_REPOSITORY, useExisting: UploadedDocumentsApi },
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withInterceptors([authInterceptor, errorInterceptor])),
    provideAnimationsAsync(),
    provideToastr({
      timeOut: 3500,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
      progressBar: true,
    }),
  ],
};
