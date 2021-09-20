import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {Subscription} from "rxjs";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {NotificationService} from "../../service/notification.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {UserService} from "../../service/user.service";
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit, OnDestroy {
  subscriptions: Subscription = new Subscription();

  selectedDic: string = 'lock';
  actData: any;

  constructor(private userService: UserService,
              private notifyService: NotificationService,
              private ngxLoader: NgxUiLoaderService) {
  }

  ngOnInit(): void {
    this.getListBlockUser();
  }

  getListBlockUser() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(this.userService.getListBlockUser()
      .subscribe(res => {
        this.actData = res;
      }));
    this.ngxLoader.stopBackground()
  }

  setSelectedDic(code: string) {
    this.selectedDic = code;
    if (code == 'lock') {
      this.getListBlockUser();
    }
  }


  unlock(obj: any) {
    this.ngxLoader.startBackground();

    this.subscriptions.add(
      this.userService.unlockUser(obj.empId).subscribe(
        res => {
          this.notifyService.showInfo('Разблокирован', '');
          this.getListBlockUser();
        }
      )
    )
    this.ngxLoader.stopBackground()

  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
