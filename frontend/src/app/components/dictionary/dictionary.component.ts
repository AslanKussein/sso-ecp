import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {SystemService} from "../../service/system.service";
import {NotificationService} from "../../service/notification.service";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {Subscription} from "rxjs";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {Util} from "../../service/util";
import {FormBuilder, Validators} from "@angular/forms";
import {TranslateService} from "@ngx-translate/core";

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
  dAlias!: any;
  blockUserList!: any;
  systemForm: any;

  constructor(private systemService: SystemService,
              private notifyService: NotificationService,
              private modalService: BsModalService,
              private util: Util,
              private translate: TranslateService,
              private formBuilder: FormBuilder,
              private ngxLoader: NgxUiLoaderService) {
  }

  ngOnInit(): void {
    if (this.util.getCurrentUser().isAdmin) {
      this.getSystems();
      this.getDAlias();
      this.getBlockUserList();
    }

    this.systemForm = this.formBuilder.group({
      id: ['', Validators.nullValidator],
      name: ['', Validators.required],
      url: ['', Validators.required],
      aliasCode: ['', Validators.required]
    });
  }

  getSystems() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(this.systemService.getAll()
      .subscribe(res => {
        this.actData = res;
        this.ngxLoader.stopBackground()
      }, () => {
        this.ngxLoader.stopBackground()
      }));

  }

  setSelectedDic(code: string) {
    this.selectedDic = code;
    if (code == 'systems') {
      this.getSystems();
    }
  }

  setValue(controlName: string, value: any) {
    this.systemForm.controls[controlName].setValue(value);
    this.systemForm.controls[controlName].updateValueAndValidity();
  }

  editData(data: any) {
    this.setValue('id', data.id);
    this.setValue('name', data.name);
    this.setValue('url', data.url);
    this.setValue('aliasCode', data.aliasCode);

    console.log(this.systemForm)
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, {backdrop: 'static', keyboard: false});
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  saveSystems() {
    if (this.systemForm.value.id == null) {
      this.createSystem();
    } else {
      this.updateSystems();
    }
  }

  createSystem() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(this.systemService.create(this.systemForm?.value)
      .subscribe(res => {
        this.getSystems();
        this.notifyService.showInfo('Успешно', '');
        this.modalRef.hide();
        this.ngxLoader.stopBackground()
      }, () => {
        this.ngxLoader.stopBackground()
      }));
  }

  updateSystems() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(this.systemService.edit(this.systemForm.value.id, this.systemForm?.value)
      .subscribe(res => {
        this.getSystems();
        this.notifyService.showInfo('Успешно', '');
        this.modalRef.hide();
        this.ngxLoader.stopBackground()
      }, () => {
        this.ngxLoader.stopBackground()
      }));
  }

  getDAlias() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(
      this.systemService.getDAlias().subscribe(e => {
        this.dAlias = e;
        this.ngxLoader.stopBackground()
      }, () => {
        this.ngxLoader.stopBackground()
      })
    )
  }

  getBlockUserList() {
    this.ngxLoader.startBackground();
    this.subscriptions.add(
      this.systemService.getBlockUserList().subscribe(e => {
        this.blockUserList = e;
        this.ngxLoader.stopBackground()
      }, () => {
        this.ngxLoader.stopBackground()
      })
    )
  }

  unBlockUser(empId: number) {
    this.ngxLoader.startBackground();
    this.subscriptions.add(
      this.systemService.unBlockUser(empId).subscribe(e => {
        this.getBlockUserList();
        this.ngxLoader.stopBackground()
      }, () => {
        this.ngxLoader.stopBackground()
      })
    )
  }

  createNewSystem() {
    this.systemForm.reset();
  }


  removeSystem(data: any) {
    if (confirm(this.translate.instant('dic.confirmRemove') + " " + data.name + " ?")) {
      this.ngxLoader.startBackground();
      this.subscriptions.add(this.systemService.remove(data.id)
        .subscribe(res => {
          this.getSystems();
          this.notifyService.showInfo('Успешно', '');
          this.modalRef.hide();
          this.ngxLoader.stopBackground()
        }, () => {
          this.ngxLoader.stopBackground()
        }));
    }
  }
}
