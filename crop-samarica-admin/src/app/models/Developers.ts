export interface Developers {
  id: string;
  name: string;
  profile: string;
  email: string;
  roles: DeveloperRoles[];
}

export enum DeveloperRoles {
  ANDRIOD_DEVELOPER = 'ANDROID DEVELOPER',
  WEB_DEVELOPER = 'WEB DEVELOPER',
  UI_UX_DESIGNER = 'UI/UX DESIGNER',
  RESEARCHER = 'RESEARCHER',
  DATA_ANALYST = 'DATA ANALYST',
}
