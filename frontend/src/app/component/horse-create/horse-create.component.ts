import { Component, OnInit } from '@angular/core';
import {Horse} from "../../dto/horse";
import {HorseService} from "../../service/horse.service";

@Component({
  selector: 'app-horse-create',
  templateUrl: './horse-create.component.html',
  styleUrls: ['./horse-create.component.scss']
})
export class HorseCreateComponent implements OnInit {

  horse: Horse;
  savedSuccess = false;
  error?: string;

  constructor(
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.horse = {} as Horse;
  }

  createHorse(horse: Horse): void {
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
