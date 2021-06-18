import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {SystemService} from "../../service/system.service";
import {NotificationService} from "../../service/notification.service";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {Subscription} from "rxjs";
import {NgxUiLoaderService} from "ngx-ui-loader";

@Component({
  selector: 'app-dictionary',
  templateUrl: './dictionary.component.html',
  styleUrls: ['./dictionary.component.scss']
})
export class DictionaryComponent implements OnInit, OnDestroy {
  subscriptions: Subscription = new Subscription();

  selectedDic: string = 'systems';
  actData: any;
  modalRef!: BsModalRef;
  edData: any;

  constructor(private systemService: SystemService,
              private notifyService: NotificationService,
              private modalService: BsModalService,
              private ngxLoader: NgxUiLoaderService) {
  }

  ngOnInit(): void {
    this.getSystems();
  }

  getSystems() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(this.systemService.getAll()
      .subscribe(res => {
        this.actData = res;
      }));
    this.ngxLoader.stopBackground()
  }

  setSelectedDic(code: string) {
    this.selectedDic = code;
    if (code == 'systems') {
      this.getSystems();
    }
  }

  editData(data: any) {
    this.edData = data;
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, {backdrop: 'static', keyboard: false});
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  saveSystems() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(this.systemService.edit(this.edData.id, this.edData)
      .subscribe(res => {
        this.actData = res;
        this.notifyService.showInfo('Успешно', '');
        this.modalRef.hide()
      }));
    this.ngxLoader.stopBackground()
  }

  remove() {
    this.modalRef.hide();
  }
}
