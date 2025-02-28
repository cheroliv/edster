import { IUser } from 'app/shared/model/user.model';

export interface IPresentation {
  id?: number;
  name?: string;
  plan?: string | null;
  uri?: string | null;
  user?: IUser;
}

export const defaultValue: Readonly<IPresentation> = {};
