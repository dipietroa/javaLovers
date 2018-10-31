import { Component, OnInit } from '@angular/core';
import { CommentsService } from '../api/comments.service';
import { Comment } from '../model/comment';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css']
})
export class CardsComponent implements OnInit {

  comments : Array<Comment>

  constructor(private commentService : CommentsService) { }

  ngOnInit() {
    this.commentService.getComments().subscribe((res) => {
      this.comments = res;
    }, (err) => {
      alert('Problem when connecting to the server -- status : ' + err.status)
    })
  }

  public displayDate(date : Date) : string {
    let disDate = new Date(date);
    
    return this.formatNum(disDate.getDate()) + "/" + this.formatNum(disDate.getMonth()) + "/" 
          + disDate.getFullYear() + " at " + this.formatNum(disDate.getHours()) + "h" 
          + this.formatNum(disDate.getMinutes())
  }

  private formatNum(num : number) : string {
    return num < 10 ? '0' + num : '' + num;
  }

}
