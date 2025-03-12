import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './plant-uml-diagram-resource.reducer';

export const PlantUMLDiagramResourceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const plantUMLDiagramResourceEntity = useAppSelector(state => state.plantUMLDiagramResource.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="plantUMLDiagramResourceDetailsHeading">
          <Translate contentKey="edsterApp.plantUMLDiagramResource.detail.title">PlantUMLDiagramResource</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{plantUMLDiagramResourceEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="edsterApp.plantUMLDiagramResource.type">Type</Translate>
            </span>
          </dt>
          <dd>{plantUMLDiagramResourceEntity.type}</dd>
          <dt>
            <span id="uri">
              <Translate contentKey="edsterApp.plantUMLDiagramResource.uri">Uri</Translate>
            </span>
          </dt>
          <dd>{plantUMLDiagramResourceEntity.uri}</dd>
          <dt>
            <span id="umlCode">
              <Translate contentKey="edsterApp.plantUMLDiagramResource.umlCode">Uml Code</Translate>
            </span>
          </dt>
          <dd>{plantUMLDiagramResourceEntity.umlCode}</dd>
          <dt>
            <Translate contentKey="edsterApp.plantUMLDiagramResource.asciidocSlide">Asciidoc Slide</Translate>
          </dt>
          <dd>{plantUMLDiagramResourceEntity.asciidocSlide ? plantUMLDiagramResourceEntity.asciidocSlide.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/plant-uml-diagram-resource" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/plant-uml-diagram-resource/${plantUMLDiagramResourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlantUMLDiagramResourceDetail;
