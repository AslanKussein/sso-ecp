import {Component, OnInit} from '@angular/core';
import {SystemService} from "../../service/system.service";
import {Util} from "../../service/util";

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.scss']
})
export class DataComponent implements OnInit {
  systems: any = [];
  loading: any;

  selectedTab: number = 1;

  constructor(private systemService: SystemService,
              private util: Util) {
  }

  ngOnInit(): void {
    this.getSystems();
  }

  changeTab(id: number) {
    this.selectedTab = id;
  }

  getSystems() {
    this.loading = true;
    this.systemService.getSystemList()
      .subscribe(res => {
        this.systems = res;
      });
    this.loading = false;
  }

  redirect(uri: string) {
    let url = uri.concat('?lang=')
      .concat(this.util.nvl(localStorage.getItem('lang'), 'ru'))
      .concat('&access_token='.concat(<string>localStorage.getItem('JWT_TOKEN')))
    window.open(
      url,
      '_blank' // <- This is what makes it open in a new window.
    );
  }
}
