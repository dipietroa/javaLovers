import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommentsService } from '../api/comments.service';
import { Comment } from '../model/comment';
import { PagerService } from '../services/pager.service';
import { AuthConsts } from '../login/consts/auth.consts';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css'],
  providers: [
    PagerService
  ]
})
export class CardsComponent implements OnInit {

  comments : Array<Comment> = [];
  displayedComments : Array<Comment> = [];
  loading : boolean = false;
  nodata : boolean = false;
  pager : any = {
    totalItems: 0,
    currentPage: 1,
    pageSize: 4,
    totalPages: 1,
    startPage: 1,
    endPage: 1,
    startIndex: 1,
    endIndex: 1,
    pages: 1
  };

  constructor(private commentService : CommentsService, private pagerService : PagerService) { }

  ngOnInit() {
    this.loading = true;
    this.commentService.getComments().subscribe((res) => {
      this.comments = res.slice().reverse();
      this.setPage(1);
      this.loading = false;
    }, (err) => {
      this.nodata = true;
      this.loading = false;
      alert('Problem occurs with the server -- status : ' + err.status);
    })
  }

  displayDate(date : Date) : string {
    let disDate = new Date(date);

    return this.formatNum(disDate.getDate()) + "/" + this.formatNum(disDate.getMonth()) + "/" 
          + disDate.getFullYear() + " at " + this.formatNum(disDate.getHours()) + "h" 
          + this.formatNum(disDate.getMinutes())
  }

  addCard(comment : Comment) : void {
    this.comments.unshift(comment);
    this.setPage(1);
  }

  private formatNum(num : number) : string {
    return num < 10 ? '0' + num : '' + num;
  }

  /**
   * Set current page
   * Source : https://github.com/cornflourblue/angular2-pagination-example/blob/master/app/app.component.ts
   * @param page Current page index
   */
  setPage(page: number) {
    // get pager object from service
    this.pager = this.pagerService.getPager(this.comments.length, page, this.pager.pageSize);

    // get current page of items
    this.displayedComments = this.comments.slice(this.pager.startIndex, this.pager.endIndex + 1);
  }

  isLogged() : boolean {
    return localStorage.getItem(AuthConsts.LOCAL_STORAGE_TOKEN) != null &&
    localStorage.getItem(AuthConsts.LOCAL_STORAGE_TOKEN) != 'null' 
  }

  deleteComment(id : number) : void {
    this.commentService.deleteCommentById(id).subscribe((res) => {
      this.comments = this.comments.filter((com) => {
        return com.id != id;
      })
      this.setPage(this.pager.currentPage)
    }, (err) => {
      alert('a problem occurred with the server -- status : ' + err.status);
    })
  }

}
