import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './plant-uml-diagram-resource.reducer';

export const PlantUMLDiagramResource = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const plantUMLDiagramResourceList = useAppSelector(state => state.plantUMLDiagramResource.entities);
  const loading = useAppSelector(state => state.plantUMLDiagramResource.loading);
  const totalItems = useAppSelector(state => state.plantUMLDiagramResource.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="plant-uml-diagram-resource-heading" data-cy="PlantUMLDiagramResourceHeading">
        <Translate contentKey="edsterApp.plantUMLDiagramResource.home.title">Plant UML Diagram Resources</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="edsterApp.plantUMLDiagramResource.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/plant-uml-diagram-resource/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="edsterApp.plantUMLDiagramResource.home.createLabel">Create new Plant UML Diagram Resource</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {plantUMLDiagramResourceList && plantUMLDiagramResourceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="edsterApp.plantUMLDiagramResource.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="edsterApp.plantUMLDiagramResource.type">Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('uri')}>
                  <Translate contentKey="edsterApp.plantUMLDiagramResource.uri">Uri</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uri')} />
                </th>
                <th className="hand" onClick={sort('umlCode')}>
                  <Translate contentKey="edsterApp.plantUMLDiagramResource.umlCode">Uml Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('umlCode')} />
                </th>
                <th>
                  <Translate contentKey="edsterApp.plantUMLDiagramResource.asciidocSlide">Asciidoc Slide</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {plantUMLDiagramResourceList.map((plantUMLDiagramResource, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/plant-uml-diagram-resource/${plantUMLDiagramResource.id}`} color="link" size="sm">
                      {plantUMLDiagramResource.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`edsterApp.DocumentResourceType.${plantUMLDiagramResource.type}`} />
                  </td>
                  <td>{plantUMLDiagramResource.uri}</td>
                  <td>{plantUMLDiagramResource.umlCode}</td>
                  <td>
                    {plantUMLDiagramResource.asciidocSlide ? (
                      <Link to={`/asciidoc-slide/${plantUMLDiagramResource.asciidocSlide.id}`}>
                        {plantUMLDiagramResource.asciidocSlide.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/plant-uml-diagram-resource/${plantUMLDiagramResource.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/plant-uml-diagram-resource/${plantUMLDiagramResource.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/plant-uml-diagram-resource/${plantUMLDiagramResource.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="edsterApp.plantUMLDiagramResource.home.notFound">No Plant UML Diagram Resources found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={plantUMLDiagramResourceList && plantUMLDiagramResourceList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default PlantUMLDiagramResource;
