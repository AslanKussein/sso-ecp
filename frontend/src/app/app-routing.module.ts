import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DataComponent} from "./components/data/data.component";
import {AddSystemComponent} from "./components/add-system/add-system.component";
import {LoginComponent} from "./components/login/login.component";
import {AuthGuard} from "./directives/auth.guard";

const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'home', component: DataComponent, canActivate: [AuthGuard]},
  {path: 'systems', component: AddSystemComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {useHash: true}),
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
