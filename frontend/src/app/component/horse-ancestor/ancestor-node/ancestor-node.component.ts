import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AncestorHorse} from "../../../dto/ancestorHorse";
import {HorseService} from "../../../service/horse.service";
import {OutputHorse} from "../../../dto/outputHorse";

@Component({
  selector: 'app-ancestor-node',
  templateUrl: './ancestor-node.component.html',
  styleUrls: ['./ancestor-node.component.scss']
})
export class AncestorNodeComponent implements OnInit {

  @Input() horse: AncestorHorse;
  @Output() reloadTrigger = new EventEmitter<boolean>();
  expanded: boolean;

  constructor(
    private service: HorseService
  ) { }

  ngOnInit(): void {
    this.expanded = true;
  }

  deleteHorseById(id: number): void {
    this.service.deleteById(id).subscribe({
      next: value => {
        console.log('Deleted');
        this.reloadTrigger.emit(true);
      },
      error: err => console.log('error')
    });
  }
}
