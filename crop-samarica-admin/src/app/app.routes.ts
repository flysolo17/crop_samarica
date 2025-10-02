import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { MainComponent } from './main/main.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './main/dashboard/dashboard.component';
import { UsersComponent } from './main/users/users.component';
import { CropsComponent } from './main/crops/crops.component';
import { PestAndDiseaseComponent } from './main/pest-and-disease/pest-and-disease.component';
import { ViewPestAndDiseaseComponent } from './main/pest-and-disease/view-pest-and-disease/view-pest-and-disease.component';
import { DevelopersComponent } from './main/developers/developers.component';
import { UserGuideComponent } from './main/user-guide/user-guide.component';
import { ViewUserGuideComponent } from './main/user-guide/view-user-guide/view-user-guide.component';
import { ProfileComponent } from './main/profile/profile.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'auth',
    pathMatch: 'full',
  },
  {
    path: 'auth',
    children: [
      {
        path: '',
        component: LoginComponent,
      },
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'register',
        component: RegisterComponent,
      },
    ],
  },
  {
    path: 'main',
    component: MainComponent,
    canActivate: [authGuard], // or [authGuard] if using functional guard
    children: [
      {
        path: '',
        component: DashboardComponent,
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
      },
      {
        path: 'developers',
        component: DevelopersComponent,
      },
      {
        path: 'users',
        component: UsersComponent,
      },
      {
        path: 'crops',
        component: CropsComponent,
      },
      {
        path: 'pest-management',
        component: PestAndDiseaseComponent,
        children: [],
      },
      {
        path: 'pest-management/:id',
        component: ViewPestAndDiseaseComponent,
      },
      {
        path: 'user-guide',
        component: UserGuideComponent,
      },
      {
        path: 'user-guide/:id',
        component: ViewUserGuideComponent,
      },
      {
        path: 'profile',
        component: ProfileComponent,
      },
    ],
  },
];
