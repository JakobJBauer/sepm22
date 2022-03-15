import { Component, OnInit } from '@angular/core';
import {Horse} from '../../dto/horse';
import {ActivatedRoute, Router} from '@angular/router';
import {HorseService} from '../../service/horse.service';

@Component({
  selector: 'app-horse-details',
  templateUrl: './horse-details.component.html',
  styleUrls: ['./horse-details.component.scss']
})
export class HorseDetailsComponent implements OnInit {

  horse: Horse;
  error?: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.service.getById(params.id).subscribe({
        next: horse => {
          this.horse = horse;
        },
        error: error => {
          this.showError('Could not load horse\n' + error.message, 0);
        }
      });
    });
  }

  deleteHorseById(id: number): void {
    this.service.deleteById(id).subscribe({
      next: () => {
        this.router.navigate(['/horses']);
      },
      error: error => {
        this.showError('Could not delete horse\n' + error.message);
      }
    });
  }

  private showError(error: string, timeout: number = 10000): void {
    this.error = error;
    if (timeout !== 0) {
      setTimeout(() => {
        this.error = null;
      }, timeout);
    }
  }
}
