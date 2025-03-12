import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './link-resource.reducer';

export const LinkResourceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const linkResourceEntity = useAppSelector(state => state.linkResource.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="linkResourceDetailsHeading">
          <Translate contentKey="edsterApp.linkResource.detail.title">LinkResource</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{linkResourceEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="edsterApp.linkResource.type">Type</Translate>
            </span>
          </dt>
          <dd>{linkResourceEntity.type}</dd>
          <dt>
            <span id="uri">
              <Translate contentKey="edsterApp.linkResource.uri">Uri</Translate>
            </span>
          </dt>
          <dd>{linkResourceEntity.uri}</dd>
          <dt>
            <span id="target">
              <Translate contentKey="edsterApp.linkResource.target">Target</Translate>
            </span>
          </dt>
          <dd>{linkResourceEntity.target}</dd>
          <dt>
            <Translate contentKey="edsterApp.linkResource.asciidocSlide">Asciidoc Slide</Translate>
          </dt>
          <dd>{linkResourceEntity.asciidocSlide ? linkResourceEntity.asciidocSlide.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/link-resource" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/link-resource/${linkResourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LinkResourceDetail;
