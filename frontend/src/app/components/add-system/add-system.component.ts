import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {SystemService} from "../../service/system.service";
import {Subscription} from "rxjs";
import {BsModalService, BsModalRef} from 'ngx-bootstrap/modal';
import {NotificationService} from "../../service/notification.service";
import {a} from "@angular/core/src/render3";

@Component({
  selector: 'app-add-system',
  templateUrl: './add-system.component.html',
  styleUrls: ['./add-system.component.scss']
})
export class AddSystemComponent implements OnInit, OnDestroy {
  subscriptions: Subscription = new Subscription();

  actData: any = [];
  edData: any;
  loading: any;
  modalRef!: BsModalRef;

  constructor(private systemService: SystemService,
              private notifyService: NotificationService,
              private modalService: BsModalService) {
  }

  ngOnInit(): void {
    this.getSystems();

  }

  getSystems() {
    this.loading = true;
    this.systemService.getAll()
      .subscribe(res => {
        this.actData = res;
      });
    this.loading = false;
  }

  editData(data: any) {
    this.edData = data;
    // this.subscriptions.add();
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, {backdrop: 'static', keyboard: false});
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  save() {
    this.notifyService.showInfo('', '');
    this.modalRef.hide();
  }

  remove() {
    this.modalRef.hide();
  }
}
