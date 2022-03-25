import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Sex, sexOptions} from '../../types/sex';
import {FullHorse, ParentHorse} from '../../dto/fullHorse';
import {OutputHorse} from '../../dto/outputHorse';
import {debounceTime, distinctUntilChanged, Observable, switchMap} from 'rxjs';
import {OwnerService} from '../../service/owner.service';
import {Owner} from '../../dto/owner';
import {HorseService} from '../../service/horse.service';

@Component({
  selector: 'app-horse-form',
  templateUrl: './horse-form.component.html',
  styleUrls: ['./horse-form.component.scss']
})
export class HorseFormComponent implements OnInit {

  @Input() horse: FullHorse;
  @Input() reset = false;
  @Output() submitted = new EventEmitter<OutputHorse>();

  sexOptions = sexOptions;

  constructor(
    private ownerService: OwnerService,
    private horseService: HorseService
  ) {
  }

  ngOnInit(): void {
    if (!this.horse.parents) {
      this.horse.parents = [{} as ParentHorse, {} as ParentHorse];
    } else {
      this.horse.parents = [
        this.horse.parents.length >= 1 ? this.horse.parents[0] : {} as ParentHorse,
        this.horse.parents.length >= 2 ? this.horse.parents[1] : {} as ParentHorse,
      ];
    }
  }

  onSubmit(): void {
    if (!this.horse.parents[0] && !this.horse.parents[1]) {
      this.horse.parents = [{} as ParentHorse, {} as ParentHorse];
    }
    else {
      if(!this.horse.parents[0]) {
        this.horse.parents[0] = {} as ParentHorse;
      }
      if (!this.horse.parents[1]) {
        this.horse.parents[1] = {} as ParentHorse;
      }
    }
    console.log(this.horse.parents);
    this.submitted.emit({
      id: this.horse.id,
      name: this.horse.name,
      sex: this.horse.sex,
      description: this.horse.description,
      birthdate: this.horse.birthdate,
      ownerId: this.horse.owner ? this.horse.owner.id : null,
      parentIds: this.horse.parents.map(parent => parent.id ? parent.id : null)
    } as OutputHorse);
  }

  searchOwner = (text$: Observable<string>) => text$.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap(
      (term) => this.ownerService.getAll(
        {searchTerm: term, resultSize: 5}
      )
    )
  );

  searchParents = (text$: Observable<string>, sex: Sex) => text$.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    switchMap(
      (term) => this.horseService.getParentOptions(
        {searchTerm: term, resultSize: 5, sex}
      )
    )
  );

  searchFather = (text$: Observable<string>) => this.searchParents(text$, 'MALE');

  searchMother = (text$: Observable<string>) => this.searchParents(text$, 'FEMALE');

  ownerNameFormatter(owner: Owner): string {
    return owner.firstName + ' ' + owner.lastName;
  }

  horseNameFormatter(horse: FullHorse): string {
    return horse.name;
  }
}
