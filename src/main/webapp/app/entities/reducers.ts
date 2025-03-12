import workspace from 'app/entities/workspace/workspace.reducer';
import presentation from 'app/entities/presentation/presentation.reducer';
import asciidocSlide from 'app/entities/asciidoc-slide/asciidoc-slide.reducer';
import imageResource from 'app/entities/image-resource/image-resource.reducer';
import linkResource from 'app/entities/link-resource/link-resource.reducer';
import plantUMLDiagramResource from 'app/entities/plant-uml-diagram-resource/plant-uml-diagram-resource.reducer';
import qRCodeResource from 'app/entities/qr-code-resource/qr-code-resource.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  workspace,
  presentation,
  asciidocSlide,
  imageResource,
  linkResource,
  plantUMLDiagramResource,
  qRCodeResource,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
