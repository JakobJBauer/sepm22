import {Sex} from '../types/sex';

export interface ParentHorse {
  id?: number;
  name: string;
  birthdate?: Date;
  sex: Sex;
}
