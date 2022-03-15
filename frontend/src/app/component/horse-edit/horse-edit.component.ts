import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HorseService} from '../../service/horse.service';
import {Horse} from '../../dto/horse';
import {sexOptions} from '../../types/sex';

@Component({
  selector: 'app-horse-edit',
  templateUrl: './horse-edit.component.html',
  styleUrls: ['./horse-edit.component.scss']
})
export class HorseEditComponent implements OnInit {

  horse: Horse;
  error?: string;
  savedSuccess = false;

  sexOptions = sexOptions;

  constructor(
    private route: ActivatedRoute,
    private service: HorseService
  ) { }

  onSubmit(): void {
    this.service.update(this.horse, this.horse.id).subscribe({
      next: data => {
        this.savedSuccess = true;
        setTimeout(() => {
          this.savedSuccess = false;
        }, 5000);
      },
      error: error => {
        this.error = error.message;
        setTimeout(() => {
          this.error = null;
        }, 10000);
      }
    });
  }

}
