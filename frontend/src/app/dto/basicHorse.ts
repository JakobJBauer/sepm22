import {Sex} from "../types/sex";

export interface BasicHorse {
  id?: number;
  name: string;
  description: string;
  birthdate: Date;
  sex: Sex;
  ownerName: string;
}
