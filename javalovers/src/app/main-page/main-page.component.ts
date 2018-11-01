import { Component, OnInit, ViewChild } from '@angular/core';
import { Comment } from '../model/comment'
import { CardsComponent } from '../cards/cards.component';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  @ViewChild(CardsComponent) cards : CardsComponent;

  constructor() { }

  ngOnInit() {

  }

  addNewCard(comment : Comment) : void {
    this.cards.addCard(comment);
  }

}
