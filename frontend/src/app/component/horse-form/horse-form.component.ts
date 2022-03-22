import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BasicHorse} from '../../dto/basicHorse';
import {sexOptions} from '../../types/sex';
import {FullHorse} from '../../dto/fullHorse';
import {OutputHorse} from '../../dto/outputHorse';
import {debounceTime, distinctUntilChanged, map, Observable, OperatorFunction, switchMap} from 'rxjs';
import {OwnerService} from '../../service/owner.service';
import {Owner} from '../../dto/owner';

@Component({
  selector: 'app-horse-form',
  templateUrl: './horse-form.component.html',
  styleUrls: ['./horse-form.component.scss']
})
export class HorseFormComponent {

  @Input() horse: FullHorse;
  @Input() reset = false;
  @Output() submitted = new EventEmitter<OutputHorse>();

  sexOptions = sexOptions;

  constructor(
    private ownerService: OwnerService
  ) { }

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

  search = (text$: Observable<string>) => text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      switchMap(
        (term) =>  this.ownerService.getAll(
          {searchTerm: term, resultSize: 5}
        )
      )
    );

  ownerNameFormatter(owner: Owner): string {
    return owner.firstName + ' ' + owner.lastName;
  }
}
