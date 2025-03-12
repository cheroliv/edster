import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getWorkspaces } from 'app/entities/workspace/workspace.reducer';
import { createEntity, getEntity, reset, updateEntity } from './presentation.reducer';

export const PresentationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const workspaces = useAppSelector(state => state.workspace.entities);
  const presentationEntity = useAppSelector(state => state.presentation.entity);
  const loading = useAppSelector(state => state.presentation.loading);
  const updating = useAppSelector(state => state.presentation.updating);
  const updateSuccess = useAppSelector(state => state.presentation.updateSuccess);

  const handleClose = () => {
    navigate(`/presentation${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getWorkspaces({}));
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
      ...presentationEntity,
      ...values,
      workspace: workspaces.find(it => it.id.toString() === values.workspace?.toString()),
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
          ...presentationEntity,
          workspace: presentationEntity?.workspace?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="edsterApp.presentation.home.createOrEditLabel" data-cy="PresentationCreateUpdateHeading">
            <Translate contentKey="edsterApp.presentation.home.createOrEditLabel">Create or edit a Presentation</Translate>
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
                  id="presentation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('edsterApp.presentation.plan')}
                id="presentation-plan"
                name="plan"
                data-cy="plan"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('edsterApp.presentation.uri')}
                id="presentation-uri"
                name="uri"
                data-cy="uri"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('edsterApp.presentation.promptUserMessage')}
                id="presentation-promptUserMessage"
                name="promptUserMessage"
                data-cy="promptUserMessage"
                type="text"
              />
              <ValidatedField
                id="presentation-workspace"
                name="workspace"
                data-cy="workspace"
                label={translate('edsterApp.presentation.workspace')}
                type="select"
              >
                <option value="" key="0" />
                {workspaces
                  ? workspaces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/presentation" replace color="info">
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

export default PresentationUpdate;
