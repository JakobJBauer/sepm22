import {Sex} from "../types/sex";

export interface Horse {
  id?: number;
  name: string;
  description: string;
  birthdate: Date;
  sex: Sex;
  owner: string;
}
