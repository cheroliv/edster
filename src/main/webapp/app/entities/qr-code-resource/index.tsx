import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QRCodeResource from './qr-code-resource';
import QRCodeResourceDetail from './qr-code-resource-detail';
import QRCodeResourceUpdate from './qr-code-resource-update';
import QRCodeResourceDeleteDialog from './qr-code-resource-delete-dialog';

const QRCodeResourceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QRCodeResource />} />
    <Route path="new" element={<QRCodeResourceUpdate />} />
    <Route path=":id">
      <Route index element={<QRCodeResourceDetail />} />
      <Route path="edit" element={<QRCodeResourceUpdate />} />
      <Route path="delete" element={<QRCodeResourceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QRCodeResourceRoutes;
