import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PlantUMLDiagramResource from './plant-uml-diagram-resource';
import PlantUMLDiagramResourceDetail from './plant-uml-diagram-resource-detail';
import PlantUMLDiagramResourceUpdate from './plant-uml-diagram-resource-update';
import PlantUMLDiagramResourceDeleteDialog from './plant-uml-diagram-resource-delete-dialog';

const PlantUMLDiagramResourceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PlantUMLDiagramResource />} />
    <Route path="new" element={<PlantUMLDiagramResourceUpdate />} />
    <Route path=":id">
      <Route index element={<PlantUMLDiagramResourceDetail />} />
      <Route path="edit" element={<PlantUMLDiagramResourceUpdate />} />
      <Route path="delete" element={<PlantUMLDiagramResourceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlantUMLDiagramResourceRoutes;
