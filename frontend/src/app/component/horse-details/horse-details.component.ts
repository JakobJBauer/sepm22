import { Component, OnInit } from '@angular/core';
import {Horse} from '../../dto/horse';
import {ActivatedRoute} from '@angular/router';
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
}
