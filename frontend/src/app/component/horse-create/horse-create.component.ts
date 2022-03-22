import { Component, OnInit } from '@angular/core';
import {HorseService} from "../../service/horse.service";
import {OutputHorse} from "../../dto/outputHorse";
import {FullHorse} from "../../dto/fullHorse";

@Component({
  selector: 'app-horse-create',
  templateUrl: './horse-create.component.html',
  styleUrls: ['./horse-create.component.scss']
})
export class HorseCreateComponent implements OnInit {

  horse: FullHorse;
  savedSuccess = false;
  error?: string;

  constructor(
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.horse = {} as FullHorse;
  }

  createHorse(horse: OutputHorse): void {
    this.service.create(horse).subscribe({
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
