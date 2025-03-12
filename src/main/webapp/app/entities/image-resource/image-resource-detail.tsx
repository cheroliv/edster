import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './image-resource.reducer';

export const ImageResourceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imageResourceEntity = useAppSelector(state => state.imageResource.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imageResourceDetailsHeading">
          <Translate contentKey="edsterApp.imageResource.detail.title">ImageResource</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{imageResourceEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="edsterApp.imageResource.type">Type</Translate>
            </span>
          </dt>
          <dd>{imageResourceEntity.type}</dd>
          <dt>
            <span id="uri">
              <Translate contentKey="edsterApp.imageResource.uri">Uri</Translate>
            </span>
          </dt>
          <dd>{imageResourceEntity.uri}</dd>
          <dt>
            <span id="resolution">
              <Translate contentKey="edsterApp.imageResource.resolution">Resolution</Translate>
            </span>
          </dt>
          <dd>{imageResourceEntity.resolution}</dd>
          <dt>
            <Translate contentKey="edsterApp.imageResource.asciidocSlide">Asciidoc Slide</Translate>
          </dt>
          <dd>{imageResourceEntity.asciidocSlide ? imageResourceEntity.asciidocSlide.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/image-resource" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/image-resource/${imageResourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImageResourceDetail;
