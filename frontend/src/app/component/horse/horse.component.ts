import {Component, OnInit} from '@angular/core';
import {Horse} from '../../dto/horse';
import {HorseService} from 'src/app/service/horse.service';

@Component({
  selector: 'app-horse',
  templateUrl: './horse.component.html',
  styleUrls: ['./horse.component.scss']
})
export class HorseComponent implements OnInit {
  search = false;
  horses: Horse[];
  error?: string;

  constructor(
    private service: HorseService,
  ) { }

  ngOnInit(): void {
    this.reloadHorses();
  }

  reloadHorses() {
    this.service.getAll().subscribe({
      next: data => {
        console.log('received horses', data);
        this.horses = data;
      },
      error: error => {
        console.error('Error fetching horses', error.message);
        this.showError('Could not fetch horses: ' + error.message);
      }
    });
  }

  deleteHorseById(id: number): void {
    this.service.deleteById(id).subscribe({
      next: () => this.reloadHorses(),
      error: err => {
        this.showError(err.message);
        this.reloadHorses();
      }
    });
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
