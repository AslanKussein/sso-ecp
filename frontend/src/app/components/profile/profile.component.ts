import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Util} from "../../service/util";
import {UserService} from "../../service/user.service";
import {NotificationService} from "../../service/notification.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {AbstractControl, FormBuilder, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {PageDto} from "../../models/pageDto";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  subscriptions: Subscription = new Subscription();
  selectedTab: number = 1;
  paymentTypes!: any;
  appForm: any;

  actData: any;
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 0;
  page: any = PageDto;

  constructor(private userService: UserService,
              private notifyService: NotificationService,
              public util: Util,
              private formBuilder: FormBuilder,
              private ngxLoader: NgxUiLoaderService) {
  }

  get f() {
    return this.appForm.controls;
  }

  changeTab(id: number) {
    this.selectedTab = id;
  }

  ngOnInit(): void {
    this.appForm = this.formBuilder.group({
      password: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      reNewPassword: ['', [Validators.required, Validators.minLength(6)]]
    })
    this.loadAction(1);
  }


  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  changePassword() {
    this.ngxLoader.startBackground();

    this.subscriptions.add(
      this.userService.changePassword(this.appForm.getRowValue()).subscribe(e=> {
        this.ngxLoader.stopBackground();
      }, error => {
        this.ngxLoader.stopBackground();
        return
      })
    );
  }

  pageChanged(event: any): void {
    console.log('pageNo ', event)

    if (this.currentPage !== event.page) {
      this.loadAction(event.page);

    }
  }

  loadAction(pageNo: number) {
    this.page = new PageDto();
    this.page.pageNumber = pageNo;
    this.page.pageSize = this.itemsPerPage;
    this.ngxLoader.startBackground()
    this.subscriptions.add(
      this.userService.getUserRole(this.page).subscribe(res => {
        this.ngxLoader.stopBackground()
        this.actData = res.content;
        this.totalItems = res.totalElements;
        this.currentPage = pageNo;

      }, () => {
        this.ngxLoader.stopBackground();
        return;
      })
    )
  }
}
