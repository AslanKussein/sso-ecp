import {Component, ElementRef, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {NgxUiLoaderService} from "ngx-ui-loader";
import {AuthenticationService} from "../../service/authentication.service";
import {NotificationService} from "../../service/notification.service";
import {Util} from "../../service/util";
import {BsModalRef, BsModalService} from "ngx-bootstrap/modal";
import {TranslateService} from "@ngx-translate/core";

declare function callSignXml(p: () => void): any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  mySelect: any = [
    {code: "ru", value: 'Рус'},
    {code: "kz", value: 'Қаз'}
  ];
  @Input() imgPath: string = "../../../assets/images/img_2-min.png";

  modalRef!: BsModalRef;
  loginForm: any;
  selectedTab: number = 1;
  submitted = false;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private util: Util,
              private translate: TranslateService,
              private authenticationService: AuthenticationService,
              private notifyService: NotificationService,
              private ngxLoader: NgxUiLoaderService,
              private modalService: BsModalService) {
    if (this.authenticationService.currentUserValue) {
      this.util.dnHref('/home')
    }
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }

  selectTab(tabId: number): void {
    this.selectedTab = tabId;
    this.ngControl();
  }

  ngControl() {
    this.setValidator('username', this.selectedTab == 1 ? Validators.required : Validators.nullValidator);
    this.setValidator('password', this.selectedTab == 1 ? Validators.required : Validators.nullValidator);
    this.setValidator('certificate', this.selectedTab == 2 ? Validators.required : Validators.nullValidator);
  }

  setValidator(code: string, validator: any) {
    this.loginForm.controls[code].setValidators([validator]);
    this.loginForm.controls[code].updateValueAndValidity();
  }

  ngOnInit(): void {
    this.mySelect - this.util.getLang();
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      lang: ['', Validators.nullValidator],
      certificate: ['', Validators.nullValidator],
      accept: ['', Validators.requiredTrue]
    });
    if (this.util.getItem('lang') == null) {
      this.setValue('lang', 'ru');
      this.util.setItem('lang', 'ru');
    } else {
      this.setValue('lang', this.util.getItem('lang'));
    }
    this.translate.use(<string>localStorage.getItem('lang'));
  }

  setValue(controlName: string, value: any) {
    this.loginForm.controls[controlName].setValue(value);
    this.loginForm.controls[controlName].updateValueAndValidity();
  }

  get f() {
    return this.loginForm.controls;
  }

  changeLang() {
    this.util.setItem('lang', this.loginForm.value.lang);
    this.translate.use(this.loginForm.value.lang);
  }

  login() {
    this.ngxLoader.startBackground()
    this.submitted = true;
    if (this.selectedTab == 1) {
      this.loginForm.value.certificate = null;
    }
    if (this.selectedTab == 2) {
      this.loginForm.value.username = null;
      this.loginForm.value.password = null;
    }

    this.authenticationService.login(this.loginForm);

    this.ngxLoader.stopBackground()
  }

  checkCert(e: any) {
    let me = this;
    callSignXml(function () {
      // @ts-ignore
      me.loginForm.value.certificate = document.getElementById('certificate').value;
      me.login();
    });
  }
}
