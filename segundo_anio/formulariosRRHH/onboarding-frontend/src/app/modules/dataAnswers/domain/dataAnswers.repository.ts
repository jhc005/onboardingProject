import { Observable } from 'rxjs';
import { DataAnswer, SaveAnswersRequest } from './dataAnswers';

export interface IDataAnswersRepository {
  save(processId: string, request: SaveAnswersRequest): Observable<DataAnswer[]>;
  findByProcess(processId: string): Observable<DataAnswer[]>;
  findByProcessAdmin(processId: string): Observable<DataAnswer[]>;
}
