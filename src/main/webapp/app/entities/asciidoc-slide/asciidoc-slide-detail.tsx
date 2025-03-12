import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './asciidoc-slide.reducer';

export const AsciidocSlideDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const asciidocSlideEntity = useAppSelector(state => state.asciidocSlide.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="asciidocSlideDetailsHeading">
          <Translate contentKey="edsterApp.asciidocSlide.detail.title">AsciidocSlide</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{asciidocSlideEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="edsterApp.asciidocSlide.title">Title</Translate>
            </span>
          </dt>
          <dd>{asciidocSlideEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="edsterApp.asciidocSlide.content">Content</Translate>
            </span>
          </dt>
          <dd>{asciidocSlideEntity.content}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="edsterApp.asciidocSlide.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{asciidocSlideEntity.notes}</dd>
          <dt>
            <span id="num">
              <Translate contentKey="edsterApp.asciidocSlide.num">Num</Translate>
            </span>
          </dt>
          <dd>{asciidocSlideEntity.num}</dd>
          <dt>
            <Translate contentKey="edsterApp.asciidocSlide.presentation">Presentation</Translate>
          </dt>
          <dd>{asciidocSlideEntity.presentation ? asciidocSlideEntity.presentation.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/asciidoc-slide" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/asciidoc-slide/${asciidocSlideEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AsciidocSlideDetail;
