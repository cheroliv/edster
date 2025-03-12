import { IUser } from 'app/shared/model/user.model';

export interface IWorkspace {
  id?: number;
  name?: string;
  description?: string | null;
  promptSystemMessage?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IWorkspace> = {};
