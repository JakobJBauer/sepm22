import {Sex} from '../types/sex';

export interface SearchParams {
  name?: string;
  description?: string;
  birthdate?: Date;
  sex?: Sex;
  ownerName?: string;
}
