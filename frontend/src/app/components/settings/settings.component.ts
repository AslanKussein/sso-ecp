import {ChangeDetectorRef, Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {Subscription} from "rxjs";
import {NotificationService} from "../../service/notification.service";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {UserService} from "../../service/user.service";
import {PageDto} from "../../models/pageDto";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit, OnDestroy {
  subscriptions: Subscription = new Subscription();

  selectedDic: string = 'lock';
  actData: any;
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 0;
  page: any = PageDto;

  constructor(private userService: UserService,
              private notifyService: NotificationService,
              private ngxLoader: NgxUiLoaderService) {
  }

  pageChanged(event: any): void {
    console.log('pageNo ', event)

    if (this.currentPage !== event.page) {
      this.loadAction(event.page);

    }
  }

  ngOnInit(): void {
    this.loadAction(1);
  }

  loadAction(pageNo: number) {
    this.page = new PageDto();
    this.page.pageNumber = pageNo;
    this.page.pageSize = this.itemsPerPage;
    this.ngxLoader.startBackground()
    this.subscriptions.add(
      this.userService.getAllAction(this.page).subscribe(res => {
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

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
