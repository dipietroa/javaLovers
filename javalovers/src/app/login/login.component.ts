import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../api/auth.service';
import { Credentials } from '../model/credentials';
import { AuthConsts } from './consts/auth.consts';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  @Output() closeReq : EventEmitter<void> = new EventEmitter();
  isSending : boolean = false;
  curLog : string = null;
  
  loginForm = new FormGroup({
    username: new FormControl('', [
      Validators.required
    ]),
    password: new FormControl('', [
      Validators.required
    ])
  })

  constructor(private authService : AuthService) { }

  ngOnInit() {
  }

  close() : void {
    this.closeReq.emit();
  }

  submit() : void {
    this.isSending = true;
    let cred : Credentials = this.loginForm.value;

    this.authService.authentication(cred).subscribe((res) => {
      localStorage.setItem(AuthConsts.LOCAL_STORAGE_USER, cred.username);
      this.close();
    }, (err) => {
      this.isSending = false;
      if(err.status === 401)
        this.curLog = 'Username or password invalid';
      else
        alert('A problem occurred with the server -- status : ' + err.status);
    })
  }

}
