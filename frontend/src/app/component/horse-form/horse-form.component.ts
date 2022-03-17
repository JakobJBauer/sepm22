import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Horse} from '../../dto/horse';
import {sexOptions} from '../../types/sex';

@Component({
  selector: 'app-horse-form',
  templateUrl: './horse-form.component.html',
  styleUrls: ['./horse-form.component.scss']
})
export class HorseFormComponent {

  @Input() horse: Horse;
  @Input() reset = false;
  @Output() submitted = new EventEmitter<Horse>();

  sexOptions = sexOptions;

  constructor() { }

  onSubmit(): void {
    this.submitted.emit(this.horse);
  }
}
