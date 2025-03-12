import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './presentation.reducer';

export const PresentationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const presentationEntity = useAppSelector(state => state.presentation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="presentationDetailsHeading">
          <Translate contentKey="edsterApp.presentation.detail.title">Presentation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{presentationEntity.id}</dd>
          <dt>
            <span id="plan">
              <Translate contentKey="edsterApp.presentation.plan">Plan</Translate>
            </span>
          </dt>
          <dd>{presentationEntity.plan}</dd>
          <dt>
            <span id="uri">
              <Translate contentKey="edsterApp.presentation.uri">Uri</Translate>
            </span>
          </dt>
          <dd>{presentationEntity.uri}</dd>
          <dt>
            <span id="promptUserMessage">
              <Translate contentKey="edsterApp.presentation.promptUserMessage">Prompt User Message</Translate>
            </span>
          </dt>
          <dd>{presentationEntity.promptUserMessage}</dd>
          <dt>
            <Translate contentKey="edsterApp.presentation.workspace">Workspace</Translate>
          </dt>
          <dd>{presentationEntity.workspace ? presentationEntity.workspace.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/presentation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/presentation/${presentationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PresentationDetail;
