import {Sex} from '../types/sex';

export interface OutputHorse {
  id?: number;
  name: string;
  description: string;
  birthdate: Date;
  sex: Sex;
  ownerId: number;
  parentIds: number[];
}
