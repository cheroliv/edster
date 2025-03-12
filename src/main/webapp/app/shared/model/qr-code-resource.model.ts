import { IAsciidocSlide } from 'app/shared/model/asciidoc-slide.model';
import { DocumentResourceType } from 'app/shared/model/enumerations/document-resource-type.model';

export interface IQRCodeResource {
  id?: number;
  type?: keyof typeof DocumentResourceType | null;
  uri?: string | null;
  data?: string | null;
  asciidocSlide?: IAsciidocSlide | null;
}

export const defaultValue: Readonly<IQRCodeResource> = {};
