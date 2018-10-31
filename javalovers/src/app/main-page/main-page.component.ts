import { Component, OnInit } from '@angular/core';
import { CommentsService } from '../api/comments.service';
import { Comment } from '../model/comment'

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  comments : Array<Comment>

  constructor(private commentService : CommentsService) { }

  ngOnInit() {
    this.commentService.getComments().subscribe((res) => {
      this.comments = res;
    }, (err) => {
      alert('Problem when connecting to the server -- status : ' + err.status)
    })
  }

}