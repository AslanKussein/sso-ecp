import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DataComponent} from "./components/data/data.component";
import {LoginComponent} from "./components/login/login.component";
import {AuthGuard} from "./directives/auth.guard";
import {HeaderComponent} from "./components/header/header.component";

const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'home', component: DataComponent, canActivate: [AuthGuard]},
  {path: 'header', component: HeaderComponent, canActivate: [AuthGuard]},
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
