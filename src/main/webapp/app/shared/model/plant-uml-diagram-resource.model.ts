import { IAsciidocSlide } from 'app/shared/model/asciidoc-slide.model';
import { DocumentResourceType } from 'app/shared/model/enumerations/document-resource-type.model';

export interface IPlantUMLDiagramResource {
  id?: number;
  type?: keyof typeof DocumentResourceType | null;
  uri?: string | null;
  umlCode?: string | null;
  asciidocSlide?: IAsciidocSlide | null;
}

export const defaultValue: Readonly<IPlantUMLDiagramResource> = {};
