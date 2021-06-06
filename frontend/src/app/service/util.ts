import {Injectable} from '@angular/core';
import {language} from "../../environments/language";
import {ActivatedRoute, Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";


@Injectable({
  providedIn: 'root'
})
export class Util {
  _language = language;
  mySelect: any;

  constructor(private router: Router,
              protected route: ActivatedRoute,
              protected translate: TranslateService) {
  }

  dnHref(href: any) {
    this.setItem('url', href);
    this.router.navigateByUrl(href);
  }

  getLang() {
    this.mySelect = [
      {code: "ru", value: 'Рус'},
      {code: "kz", value: 'Қаз'}
    ];
    return this.mySelect;
  }

  getLangValue(): string {
    let value = 'Рус';
    let code = this.getItem('lang')
    this.getLang().forEach(function (element: any) {
      if (element.code == code) {
        value = element.value;
      }
    });
    return value;
  }

  dnHrefParam(href: any, param: any) {
    this.router.navigate([href], {queryParams: {activeTab: param, fromBoard: true}});
  }

  navigateByUrl(href: any) {
    this.router.navigateByUrl(href, {replaceUrl: true});
  }

  isNullOrEmpty(e: any) {
    return e == null || e == '' || e == undefined;
  }

  getCurrentUser() {
    return JSON.parse(<string>localStorage.getItem('currentUser'))
  }

  setItem(key: string, value: string) {
    localStorage.setItem(key, value);
  }

  getItem(key: string) {
    return localStorage.getItem(key);
  }

  removeItem(key: string) {
    localStorage.removeItem(key);
  }

  getError() {
    let fieldName;
    switch (this._language.language) {
      case "kz":
        fieldName = 'kk';
        break;
      case "en":
        fieldName = 'en';
        break;
      default:
        fieldName = 'ru';
        break;
    }
    return fieldName;
  }

  nvl(val: any, val2: any) {
    return this.isNullOrEmpty(val) ? val2 : val;
  }
}
