import {Observable} from "rxjs";
import {CanDeactivate} from "@angular/router";
import {ComponentCanDeactivate} from "./componentCanDeactivate";

export class ExitDeactivate implements CanDeactivate<ComponentCanDeactivate>{

  canDeactivate(component: ComponentCanDeactivate) : Observable<boolean> | boolean{
    return component.canDeactivate ? component.canDeactivate() : true;
  }
}
