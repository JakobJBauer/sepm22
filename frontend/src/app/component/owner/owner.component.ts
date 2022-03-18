import { Component, OnInit } from '@angular/core';
import {OwnerService} from "../../service/owner.service";
import {Owner} from "../../dto/owner";

@Component({
  selector: 'app-owner',
  templateUrl: './owner.component.html',
  styleUrls: ['./owner.component.scss']
})
export class OwnerComponent implements OnInit {

  owners: Owner[];
  error?: string;

  constructor(
    private service: OwnerService
  ) { }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll() {
    this.service.getAll().subscribe({
      next: data => {
        console.log('received owners', data);
        this.owners = data;
      },
      error: error => {
        console.error('Error fetching owners', error.message);
        this.showError('Could not fetch owners:\n' + error.message);
      }
    });
  }

  private showError(msg: string) {
    this.error = msg;
    setTimeout(() => {
      this.error = null;
    }, 10000);
  }

}
