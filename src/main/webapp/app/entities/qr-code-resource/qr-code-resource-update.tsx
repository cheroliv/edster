import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAsciidocSlides } from 'app/entities/asciidoc-slide/asciidoc-slide.reducer';
import { DocumentResourceType } from 'app/shared/model/enumerations/document-resource-type.model';
import { createEntity, getEntity, reset, updateEntity } from './qr-code-resource.reducer';

export const QRCodeResourceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const asciidocSlides = useAppSelector(state => state.asciidocSlide.entities);
  const qRCodeResourceEntity = useAppSelector(state => state.qRCodeResource.entity);
  const loading = useAppSelector(state => state.qRCodeResource.loading);
  const updating = useAppSelector(state => state.qRCodeResource.updating);
  const updateSuccess = useAppSelector(state => state.qRCodeResource.updateSuccess);
  const documentResourceTypeValues = Object.keys(DocumentResourceType);

  const handleClose = () => {
    navigate(`/qr-code-resource${location.search}`);
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
      ...qRCodeResourceEntity,
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
          ...qRCodeResourceEntity,
          asciidocSlide: qRCodeResourceEntity?.asciidocSlide?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edsterApp.qRCodeResource.home.createOrEditLabel" data-cy="QRCodeResourceCreateUpdateHeading">
            <Translate contentKey="edsterApp.qRCodeResource.home.createOrEditLabel">Create or edit a QRCodeResource</Translate>
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
                  id="qr-code-resource-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('edsterApp.qRCodeResource.type')}
                id="qr-code-resource-type"
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
                label={translate('edsterApp.qRCodeResource.uri')}
                id="qr-code-resource-uri"
                name="uri"
                data-cy="uri"
                type="text"
              />
              <ValidatedField
                label={translate('edsterApp.qRCodeResource.data')}
                id="qr-code-resource-data"
                name="data"
                data-cy="data"
                type="text"
              />
              <ValidatedField
                id="qr-code-resource-asciidocSlide"
                name="asciidocSlide"
                data-cy="asciidocSlide"
                label={translate('edsterApp.qRCodeResource.asciidocSlide')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/qr-code-resource" replace color="info">
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

export default QRCodeResourceUpdate;
