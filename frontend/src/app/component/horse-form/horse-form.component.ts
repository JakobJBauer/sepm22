import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Horse} from '../../dto/horse';
import {BasicHorse} from '../../dto/basicHorse';
import {sexOptions} from '../../types/sex';
import {FullHorse} from '../../dto/fullHorse';
import {OutputHorse} from '../../dto/outputHorse';
import {Owner} from '../../dto/owner';

@Component({
  selector: 'app-horse-form',
  templateUrl: './horse-form.component.html',
  styleUrls: ['./horse-form.component.scss']
})
export class HorseFormComponent {

  @Input() horse: Horse;
  @Input() horse: FullHorse;
  @Input() reset = false;
  @Output() submitted = new EventEmitter<Horse>();
  @Output() submitted = new EventEmitter<OutputHorse>();

  sexOptions = sexOptions;

  constructor() { }

  onSubmit(): void {
    this.submitted.emit({
      id: this.horse.id,
      name: this.horse.name,
      sex: this.horse.sex,
      description: this.horse.description,
      birthdate: this.horse.birthdate,
      ownerId: this.horse.owner.id
    } as unknown as OutputHorse);
  }
  }
}
