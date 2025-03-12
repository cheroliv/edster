import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Workspace from './workspace';
import Presentation from './presentation';
import AsciidocSlide from './asciidoc-slide';
import ImageResource from './image-resource';
import LinkResource from './link-resource';
import PlantUMLDiagramResource from './plant-uml-diagram-resource';
import QRCodeResource from './qr-code-resource';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="workspace/*" element={<Workspace />} />
        <Route path="presentation/*" element={<Presentation />} />
        <Route path="asciidoc-slide/*" element={<AsciidocSlide />} />
        <Route path="image-resource/*" element={<ImageResource />} />
        <Route path="link-resource/*" element={<LinkResource />} />
        <Route path="plant-uml-diagram-resource/*" element={<PlantUMLDiagramResource />} />
        <Route path="qr-code-resource/*" element={<QRCodeResource />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
