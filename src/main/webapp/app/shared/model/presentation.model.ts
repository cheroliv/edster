import { IWorkspace } from 'app/shared/model/workspace.model';

export interface IPresentation {
  id?: number;
  plan?: string;
  uri?: string;
  promptUserMessage?: string | null;
  workspace?: IWorkspace | null;
}

export const defaultValue: Readonly<IPresentation> = {};
