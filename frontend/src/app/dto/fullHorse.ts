import {Sex} from '../types/sex';
import {Owner} from './owner';

export interface ParentHorse {
  id: number;
  birthdate: Date;
  sex: Sex;
  name: string;
}

export interface FullHorse {
  id?: number;
  name: string;
  description: string;
  birthdate: Date;
  sex: Sex;
  owner: Owner;
  parents: ParentHorse[];
}
