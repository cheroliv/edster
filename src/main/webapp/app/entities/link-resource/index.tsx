import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LinkResource from './link-resource';
import LinkResourceDetail from './link-resource-detail';
import LinkResourceUpdate from './link-resource-update';
import LinkResourceDeleteDialog from './link-resource-delete-dialog';

const LinkResourceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LinkResource />} />
    <Route path="new" element={<LinkResourceUpdate />} />
    <Route path=":id">
      <Route index element={<LinkResourceDetail />} />
      <Route path="edit" element={<LinkResourceUpdate />} />
      <Route path="delete" element={<LinkResourceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LinkResourceRoutes;
