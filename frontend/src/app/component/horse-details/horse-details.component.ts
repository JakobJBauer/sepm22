import { Component, OnInit } from '@angular/core';
import {BasicHorse} from '../../dto/basicHorse';
import {ActivatedRoute, Router} from '@angular/router';
import {HorseService} from '../../service/horse.service';
import {Sex} from "../../types/sex";
import {FullHorse, ParentHorse} from "../../dto/fullHorse";

@Component({
  selector: 'app-horse-details',
  templateUrl: './horse-details.component.html',
  styleUrls: ['./horse-details.component.scss']
})
export class HorseDetailsComponent implements OnInit {

  horse: FullHorse;
  error?: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.service.getById(params.id).subscribe({
        next: horse => this.horse = horse,
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

  getParentIdBySex(sex: Sex): number {
    return this.getParentBySex(sex).id;
  }

  getParentNameBySex(sex: Sex): string {
    return this.getParentBySex(sex).name;
  }

  getParentBySex(sex: Sex): ParentHorse {
    return this.horse.parents.filter(parent => parent.sex === sex)[0];
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
