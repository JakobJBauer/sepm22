import {Sex} from '../types/sex';

export interface ParentSearchParams {
  searchTerm?: string;
  sex?: Sex;
  resultSize?: number;
}
