import {Component, OnInit} from '@angular/core';
import {User} from "../../models/users";
import {AuthenticationService} from "../../service/authentication.service";
import {Util} from "../../service/util";
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  currentUser!: User;
  langValue!: string;

  constructor(private authenticationService: AuthenticationService,
              private translate: TranslateService,
              private route: Router,
              public util: Util) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }

  ngOnInit(): void {
    this.langValue = this.util.getLangValue();
    this.translate.use(<string>localStorage.getItem('lang'));
  }

  onChange(event: any) {
    this.util.setItem('lang', event.target.id);
    this.translate.use(event.target.id);
    this.langValue = this.util.getLangValue();
  }

  logout() {
    this.authenticationService.logout();
  }
}
