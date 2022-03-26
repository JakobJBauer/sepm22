import {Component, Input, OnInit} from '@angular/core';
import {AncestorHorse} from "../../../dto/ancestorHorse";
import {HorseService} from "../../../service/horse.service";

@Component({
  selector: 'app-ancestor-node',
  templateUrl: './ancestor-node.component.html',
  styleUrls: ['./ancestor-node.component.scss']
})
export class AncestorNodeComponent implements OnInit {

  @Input() horse: AncestorHorse;
  expanded: boolean;

  constructor(
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.expanded = true;
  }

  deleteHorseById(id: number): void {
    this.service.deleteById(id).subscribe({
      next: value => console.log('Deleted'),
      error: err => console.log('error')
    });
  }
}
