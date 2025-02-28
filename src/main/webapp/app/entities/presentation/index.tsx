import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Presentation from './presentation';
import PresentationDetail from './presentation-detail';
import PresentationUpdate from './presentation-update';
import PresentationDeleteDialog from './presentation-delete-dialog';

const PresentationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Presentation />} />
    <Route path="new" element={<PresentationUpdate />} />
    <Route path=":id">
      <Route index element={<PresentationDetail />} />
      <Route path="edit" element={<PresentationUpdate />} />
      <Route path="delete" element={<PresentationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PresentationRoutes;
