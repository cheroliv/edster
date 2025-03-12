import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/workspace">
        <Translate contentKey="global.menu.entities.workspace" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/presentation">
        <Translate contentKey="global.menu.entities.presentation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/asciidoc-slide">
        <Translate contentKey="global.menu.entities.asciidocSlide" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/image-resource">
        <Translate contentKey="global.menu.entities.imageResource" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/link-resource">
        <Translate contentKey="global.menu.entities.linkResource" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/plant-uml-diagram-resource">
        <Translate contentKey="global.menu.entities.plantUmlDiagramResource" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/qr-code-resource">
        <Translate contentKey="global.menu.entities.qrCodeResource" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
