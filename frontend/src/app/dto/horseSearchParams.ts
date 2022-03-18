import {Sex} from '../types/sex';

export interface HorseSearchParams {
  name?: string;
  description?: string;
  birthdate?: Date;
  sex?: Sex;
  ownerName?: string;
}
