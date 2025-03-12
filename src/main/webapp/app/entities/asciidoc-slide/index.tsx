import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AsciidocSlide from './asciidoc-slide';
import AsciidocSlideDetail from './asciidoc-slide-detail';
import AsciidocSlideUpdate from './asciidoc-slide-update';
import AsciidocSlideDeleteDialog from './asciidoc-slide-delete-dialog';

const AsciidocSlideRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AsciidocSlide />} />
    <Route path="new" element={<AsciidocSlideUpdate />} />
    <Route path=":id">
      <Route index element={<AsciidocSlideDetail />} />
      <Route path="edit" element={<AsciidocSlideUpdate />} />
      <Route path="delete" element={<AsciidocSlideDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AsciidocSlideRoutes;
