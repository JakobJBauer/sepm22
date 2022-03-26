import { Component, OnInit } from '@angular/core';
import {HorseService} from "../../service/horse.service";
import {AncestorHorse} from "../../dto/ancestorHorse";

@Component({
  selector: 'app-horse-ancestor',
  templateUrl: './horse-ancestor.component.html',
  styleUrls: ['./horse-ancestor.component.scss']
})
export class HorseAncestorComponent implements OnInit {

  horses: AncestorHorse[];
  selectedDepth: number;
  error?: string;
  spin: boolean;

  constructor(
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.selectedDepth = 5;
    this.loadTree();
  }

  loadTree(depth: number = this.selectedDepth): void {
    this.spin = true;
    this.service.getAncestorTree(depth).subscribe({
      next: horses => this.horses = horses,
      error: err => this.error = err.message(),
      complete: () => this.spin = false
    });
  }

  onSubmit(): void {
    this.loadTree(this.selectedDepth);
  }
}
