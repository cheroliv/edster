import { IPresentation } from 'app/shared/model/presentation.model';

export interface IAsciidocSlide {
  id?: number;
  title?: string | null;
  content?: string | null;
  notes?: string | null;
  num?: number | null;
  presentation?: IPresentation | null;
}

export const defaultValue: Readonly<IAsciidocSlide> = {};
