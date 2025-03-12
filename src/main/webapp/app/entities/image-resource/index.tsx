import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ImageResource from './image-resource';
import ImageResourceDetail from './image-resource-detail';
import ImageResourceUpdate from './image-resource-update';
import ImageResourceDeleteDialog from './image-resource-delete-dialog';

const ImageResourceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ImageResource />} />
    <Route path="new" element={<ImageResourceUpdate />} />
    <Route path=":id">
      <Route index element={<ImageResourceDetail />} />
      <Route path="edit" element={<ImageResourceUpdate />} />
      <Route path="delete" element={<ImageResourceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ImageResourceRoutes;
