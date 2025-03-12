import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPresentations } from 'app/entities/presentation/presentation.reducer';
import { createEntity, getEntity, reset, updateEntity } from './asciidoc-slide.reducer';

export const AsciidocSlideUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const presentations = useAppSelector(state => state.presentation.entities);
  const asciidocSlideEntity = useAppSelector(state => state.asciidocSlide.entity);
  const loading = useAppSelector(state => state.asciidocSlide.loading);
  const updating = useAppSelector(state => state.asciidocSlide.updating);
  const updateSuccess = useAppSelector(state => state.asciidocSlide.updateSuccess);

  const handleClose = () => {
    navigate(`/asciidoc-slide${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPresentations({}));
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
    if (values.num !== undefined && typeof values.num !== 'number') {
      values.num = Number(values.num);
    }

    const entity = {
      ...asciidocSlideEntity,
      ...values,
      presentation: presentations.find(it => it.id.toString() === values.presentation?.toString()),
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
          ...asciidocSlideEntity,
          presentation: asciidocSlideEntity?.presentation?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edsterApp.asciidocSlide.home.createOrEditLabel" data-cy="AsciidocSlideCreateUpdateHeading">
            <Translate contentKey="edsterApp.asciidocSlide.home.createOrEditLabel">Create or edit a AsciidocSlide</Translate>
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
                  id="asciidoc-slide-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('edsterApp.asciidocSlide.title')}
                id="asciidoc-slide-title"
                name="title"
                data-cy="title"
                type="text"
              />
              <ValidatedField
                label={translate('edsterApp.asciidocSlide.content')}
                id="asciidoc-slide-content"
                name="content"
                data-cy="content"
                type="text"
              />
              <ValidatedField
                label={translate('edsterApp.asciidocSlide.notes')}
                id="asciidoc-slide-notes"
                name="notes"
                data-cy="notes"
                type="text"
              />
              <ValidatedField
                label={translate('edsterApp.asciidocSlide.num')}
                id="asciidoc-slide-num"
                name="num"
                data-cy="num"
                type="text"
                validate={{
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="asciidoc-slide-presentation"
                name="presentation"
                data-cy="presentation"
                label={translate('edsterApp.asciidocSlide.presentation')}
                type="select"
              >
                <option value="" key="0" />
                {presentations
                  ? presentations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/asciidoc-slide" replace color="info">
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

export default AsciidocSlideUpdate;
