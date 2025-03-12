import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAsciidocSlides } from 'app/entities/asciidoc-slide/asciidoc-slide.reducer';
import { DocumentResourceType } from 'app/shared/model/enumerations/document-resource-type.model';
import { createEntity, getEntity, reset, updateEntity } from './plant-uml-diagram-resource.reducer';

export const PlantUMLDiagramResourceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const asciidocSlides = useAppSelector(state => state.asciidocSlide.entities);
  const plantUMLDiagramResourceEntity = useAppSelector(state => state.plantUMLDiagramResource.entity);
  const loading = useAppSelector(state => state.plantUMLDiagramResource.loading);
  const updating = useAppSelector(state => state.plantUMLDiagramResource.updating);
  const updateSuccess = useAppSelector(state => state.plantUMLDiagramResource.updateSuccess);
  const documentResourceTypeValues = Object.keys(DocumentResourceType);

  const handleClose = () => {
    navigate(`/plant-uml-diagram-resource${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAsciidocSlides({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...plantUMLDiagramResourceEntity,
      ...values,
      asciidocSlide: asciidocSlides.find(it => it.id.toString() === values.asciidocSlide?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'IMAGE',
          ...plantUMLDiagramResourceEntity,
          asciidocSlide: plantUMLDiagramResourceEntity?.asciidocSlide?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edsterApp.plantUMLDiagramResource.home.createOrEditLabel" data-cy="PlantUMLDiagramResourceCreateUpdateHeading">
            <Translate contentKey="edsterApp.plantUMLDiagramResource.home.createOrEditLabel">
              Create or edit a PlantUMLDiagramResource
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="plant-uml-diagram-resource-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('edsterApp.plantUMLDiagramResource.type')}
                id="plant-uml-diagram-resource-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {documentResourceTypeValues.map(documentResourceType => (
                  <option value={documentResourceType} key={documentResourceType}>
                    {translate(`edsterApp.DocumentResourceType.${documentResourceType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('edsterApp.plantUMLDiagramResource.uri')}
                id="plant-uml-diagram-resource-uri"
                name="uri"
                data-cy="uri"
                type="text"
              />
              <ValidatedField
                label={translate('edsterApp.plantUMLDiagramResource.umlCode')}
                id="plant-uml-diagram-resource-umlCode"
                name="umlCode"
                data-cy="umlCode"
                type="text"
              />
              <ValidatedField
                id="plant-uml-diagram-resource-asciidocSlide"
                name="asciidocSlide"
                data-cy="asciidocSlide"
                label={translate('edsterApp.plantUMLDiagramResource.asciidocSlide')}
                type="select"
              >
                <option value="" key="0" />
                {asciidocSlides
                  ? asciidocSlides.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/plant-uml-diagram-resource" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PlantUMLDiagramResourceUpdate;
