export interface AncestorHorse {
  id: number;
  name: string;
  birthdate: Date;
  parents: AncestorHorse[];
}
