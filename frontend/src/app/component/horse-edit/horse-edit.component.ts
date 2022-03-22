import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HorseService} from '../../service/horse.service';
import {BasicHorse} from '../../dto/basicHorse';
import {sexOptions} from '../../types/sex';
import {OutputHorse} from "../../dto/outputHorse";
import {FullHorse} from "../../dto/fullHorse";

@Component({
  selector: 'app-horse-edit',
  templateUrl: './horse-edit.component.html',
  styleUrls: ['./horse-edit.component.scss']
})
export class HorseEditComponent implements OnInit {

  horse: FullHorse;
  error?: string;
  savedSuccess = false;

  sexOptions = sexOptions;

  constructor(
    private route: ActivatedRoute,
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.service.getById(params.id).subscribe({
        next: horse => {
          this.horse = horse;
        },
        error: error => {
          this.error = error.message;
        }
      });
    });
  }

  updateHorse(horse: OutputHorse): void {
    this.service.update(horse, horse.id).subscribe({
      next: () => {
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
