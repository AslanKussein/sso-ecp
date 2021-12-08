import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {SystemService} from "../../service/system.service";
import {Util} from "../../service/util";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.scss']
})
export class DataComponent implements OnInit, OnDestroy {
  systems: any = [];
  loading: any;
  subscriptions: Subscription = new Subscription();
  selectedTab: number = 1;

  constructor(private systemService: SystemService,
              public util: Util) {
  }

  ngOnInit(): void {
    this.getSystems();
  }

  changeTab(id: number) {
    this.selectedTab = id;
  }

  getSystems() {
    this.loading = true;
    this.subscriptions.add(
      this.systemService.getSystemList()
        .subscribe(res => {
          this.systems = res;
        })
    )
    this.loading = false;
  }

  redirect(uri: any) {
    this.subscriptions.add(
      this.systemService.redirectSystem(uri)
        .subscribe(() => {
        })
    )

    let url = uri.url.concat('?lang=')
      .concat(this.util.nvl(localStorage.getItem('lang'), 'kz'))
      .concat('&access_token='.concat(<string>localStorage.getItem('JWT_TOKEN')))
    window.open(
      url,
      '_blank' // <- This is what makes it open in a new window.
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
