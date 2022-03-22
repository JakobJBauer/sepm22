import { Component, OnInit } from '@angular/core';
import {BasicHorse} from '../../dto/basicHorse';
import {ActivatedRoute, Router} from '@angular/router';
import {HorseService} from '../../service/horse.service';
import {Sex} from "../../types/sex";

@Component({
  selector: 'app-horse-details',
  templateUrl: './horse-details.component.html',
  styleUrls: ['./horse-details.component.scss']
})
export class HorseDetailsComponent implements OnInit {

  horse: BasicHorse;
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
          this.horse = {
            id: horse.id,
            name: horse.name,
            description: horse.description,
            birthdate: horse.birthdate,
            sex: horse.sex,
            ownerName: horse.owner && horse.owner.firstName + ' ' + horse.owner.lastName
          } as BasicHorse;
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
