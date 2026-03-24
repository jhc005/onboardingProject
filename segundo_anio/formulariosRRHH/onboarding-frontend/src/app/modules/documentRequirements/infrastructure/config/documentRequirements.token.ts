import { InjectionToken } from '@angular/core';
import { IDocumentRequirementsRepository } from '../../domain/documentRequirements.repository';

export const DOCUMENT_REQUIREMENTS_REPOSITORY = new InjectionToken<IDocumentRequirementsRepository>
('IDocumentRequirementsRepository');
