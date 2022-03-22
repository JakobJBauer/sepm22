import { Component, OnInit } from '@angular/core';
import {Owner} from "../../dto/owner";
import {BasicHorse} from "../../dto/basicHorse";
import {OwnerService} from "../../service/owner.service";

@Component({
  selector: 'app-owner-create',
  templateUrl: './owner-create.component.html',
  styleUrls: ['./owner-create.component.scss']
})
export class OwnerCreateComponent implements OnInit {

  owner: Owner;
  savedSuccess = false;
  error?: string;

  constructor(
    private service: OwnerService
  ) { }

  ngOnInit(): void {
    this.owner = {} as Owner;
  }

  createOwner(): void {
    this.service.create(this.owner).subscribe({
      next: data => {
        this.savedSuccess = true;
        setTimeout(() => {
          this.savedSuccess = false;
        }, 5000);
      },
      error: error => {
        this.error = error.message;
        setTimeout(() => {
          this.error = null;
        }, 10000);
      }
    });
  }
}
