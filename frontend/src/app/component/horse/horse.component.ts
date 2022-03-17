import {Component, OnInit} from '@angular/core';
import {Horse} from '../../dto/horse';
import {HorseService} from 'src/app/service/horse.service';
import {sexOptions} from "../../types/sex";
import {SearchParams} from "../../dto/searchParams";

@Component({
  selector: 'app-horse',
  templateUrl: './horse.component.html',
  styleUrls: ['./horse.component.scss']
})
export class HorseComponent implements OnInit {
  search = false;
  horses: Horse[];
  error?: string;

  sexOptions = sexOptions;

  searchParams: SearchParams;

  constructor(
    private service: HorseService,
  ) { }

  ngOnInit(): void {
    this.reloadHorses();
    this.searchParams = {} as SearchParams;
  }

  reloadHorses() {
    this.resetForm();
    this.loadFilteredHorses();
  }

  loadFilteredHorses() {
    this.service.getAll(this.searchParams).subscribe({
      next: data => {
        console.log('received horses', data);
        this.horses = data;
      },
      error: error => {
        console.error('Error fetching horses', error.message);
        this.showError('Could not fetch horses:\n' + error.message);
      }
    });
  }

  deleteHorseById(id: number): void {
    this.service.deleteById(id).subscribe({
      next: () => this.loadFilteredHorses(),
      error: err => {
        this.showError('Could not delete horse\n' + err.message);
        this.loadFilteredHorses();
      }
    });
  }

  resetForm() {
    this.searchParams = {};
  }

  public vanishError(): void {
    this.error = null;
  }

  private showError(msg: string) {
    this.error = msg;
    setTimeout(() => {
      this.error = null;
    }, 10000);
  }
}
