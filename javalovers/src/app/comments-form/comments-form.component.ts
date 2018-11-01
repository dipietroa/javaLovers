import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { CommentsService } from '../api/comments.service';
import { Comment } from '../model/comment';

@Component({
  selector: 'app-comments-form',
  templateUrl: './comments-form.component.html',
  styleUrls: ['./comments-form.component.css']
})
export class CommentsFormComponent implements OnInit {
  @Output() saved : EventEmitter<Comment> = new EventEmitter();
  MIN_NAME : number = 3;
  MIN_TEXT : number = 1;
  MAX_NAME : number = 20;
  MAX_TEXT : number = 300;
  isSending : boolean = false;

  commentForm = new FormGroup({
    name: new FormControl('', [
      Validators.minLength(this.MIN_NAME), 
      Validators.maxLength(this.MAX_NAME),
      Validators.required
    ]),
    text: new FormControl('', [
      Validators.minLength(this.MIN_TEXT), 
      Validators.maxLength(this.MAX_TEXT),
      Validators.required
    ])
  })

  constructor(private commentsService : CommentsService) { }

  ngOnInit() {
    
  }

  remainingChars() : string {
    let remaining = this.MAX_TEXT - this.commentForm.value.text.length;
    return remaining < 0 ? 'please delete ' + -remaining + ' characters.'
                         :  remaining +  ' characters remaining.'; 
  }

  submit() : void {
    this.isSending = true;
    let toSend : Comment = this.commentForm.value;
    this.commentsService.addComment(toSend).subscribe((res) => {
      this.saved.emit(res);
      this.commentForm.get("name").setValue("");
      this.commentForm.get("text").setValue("");
      this.isSending = false;
    }, (err) => {
      alert('A problem occurred with the server -- status : ' + err.status);
      this.isSending = false;
    })
  }

}
