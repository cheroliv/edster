import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './qr-code-resource.reducer';

export const QRCodeResourceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const qRCodeResourceEntity = useAppSelector(state => state.qRCodeResource.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="qRCodeResourceDetailsHeading">
          <Translate contentKey="edsterApp.qRCodeResource.detail.title">QRCodeResource</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{qRCodeResourceEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="edsterApp.qRCodeResource.type">Type</Translate>
            </span>
          </dt>
          <dd>{qRCodeResourceEntity.type}</dd>
          <dt>
            <span id="uri">
              <Translate contentKey="edsterApp.qRCodeResource.uri">Uri</Translate>
            </span>
          </dt>
          <dd>{qRCodeResourceEntity.uri}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="edsterApp.qRCodeResource.data">Data</Translate>
            </span>
          </dt>
          <dd>{qRCodeResourceEntity.data}</dd>
          <dt>
            <Translate contentKey="edsterApp.qRCodeResource.asciidocSlide">Asciidoc Slide</Translate>
          </dt>
          <dd>{qRCodeResourceEntity.asciidocSlide ? qRCodeResourceEntity.asciidocSlide.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/qr-code-resource" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/qr-code-resource/${qRCodeResourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QRCodeResourceDetail;
