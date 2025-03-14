import React, { useState } from 'react';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row, Input, Button, Badge, Form, FormGroup, Label } from 'reactstrap';
import './workspace-landing.scss';

export const WorkspaceLanding = () => {
  const [searchText, setSearchText] = useState('');
  const [selectedTypes, setSelectedTypes] = useState({
    all: true,
    slide: false,
    site: false,
    article: false,
    training: false,
    school: false,
  });
  const [currentPage, setCurrentPage] = useState(1);

  // Sample data for content items
  const contentItems = [
    { id: 1, title: 'AsciiDoc', type: 'slide', typeCode: 'P', color: 'blue' },
    { id: 2, title: 'Kotlin', type: 'site', typeCode: 'W', color: 'red' },
    { id: 3, title: 'Python', type: 'training', typeCode: 'T', color: 'purple' },
    { id: 4, title: 'Springboot', type: 'training', typeCode: 'T', color: 'purple' },
    { id: 5, title: 'FastAPI', type: 'training', typeCode: 'T', color: 'purple' },
    { id: 6, title: 'Gradle', type: 'article', typeCode: 'A', color: 'darkSlateGrey' },
  ];

  const handleTypeChange = type => {
    if (type === 'all') {
      setSelectedTypes({
        all: !selectedTypes.all,
        slide: false,
        site: false,
        article: false,
        training: false,
        school: false,
      });
    } else {
      setSelectedTypes({
        ...selectedTypes,
        all: false,
        [type]: !selectedTypes[type],
      });
    }
  };

  const handleSearchChange = e => {
    setSearchText(e.target.value);
  };

  const typeColors = {
    slide: 'blue',
    site: 'red',
    article: 'darkorange',
    training: 'purple',
    school: 'green',
  };

  const typeCodes = {
    slide: 'P',
    site: 'W',
    article: 'A',
    training: 'T',
    school: 'S',
  };

  return (
    <div className="workspace-landing">
      {/* Top controls row with selector and new button */}
      <Row className="controls-bar">
        <Col xs={12}>
          <div className="d-flex justify-content-between align-items-center">
            <FormGroup className="content-type-select mb-0 d-flex align-items-center">
              <Input type="select" name="contentType" id="contentType" className="h-100 d-flex align-items-center">
                <option>pick one...</option>
                <option>Slide</option>
                <option>Site</option>
                <option>Article</option>
                <option>School</option>
                <option>Training</option>
              </Input>
            </FormGroup>
            <Button color="primary" className="new-button">
              New
            </Button>
          </div>
        </Col>
      </Row>

      {/* Search row below */}
      <Row className="search-bar mt-3">
        <Col xs={12}>
          <Form className="search-form d-flex align-items-stretch">
            <div className="d-flex search-container w-100">
              <Button color="secondary" className="search-button d-flex justify-content-center align-items-center">
                <i className="bi bi-search search-icon"></i>
              </Button>
              <Input type="text" placeholder="Enter text here" value={searchText} onChange={handleSearchChange} className="search-input" />
            </div>
          </Form>
        </Col>
      </Row>

      <Row className="filter-bar mt-3">
        <Col xs={12}>
          <div className="d-flex flex-wrap align-items-center">
            <span className="me-2">Select types:</span>
            <div className="filter-checkbox">
              <input type="checkbox" id="all" checked={selectedTypes.all} onChange={() => handleTypeChange('all')} />
              <label htmlFor="all" className="ms-1">
                Tous
              </label>
            </div>
            {Object.keys(typeCodes).map(type => (
              <div key={type} className="filter-checkbox">
                <input type="checkbox" id={type} checked={selectedTypes[type]} onChange={() => handleTypeChange(type)} />
                <label htmlFor={type} className="ms-1">
                  <span style={{ color: typeColors[type], fontWeight: 'bold' }}>
                    ({typeCodes[type]}) {type.charAt(0).toUpperCase() + type.slice(1)}
                  </span>
                </label>
              </div>
            ))}
          </div>
        </Col>
      </Row>

      <Row className="content-grid mt-3">
        {contentItems.map(item => (
          <Col key={item.id} md={4} className="mb-3">
            <Button color="light" className="content-item w-100 text-start" style={{ borderLeft: `5px solid ${item.color}` }}>
              <span style={{ color: item.color, fontWeight: 'bold' }}>
                ({item.typeCode}) {item.title}
              </span>
            </Button>
          </Col>
        ))}
      </Row>

      <Row className="pagination-bar mt-3">
        <Col xs={12}>
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <span>1 - 10 of 10 presentations</span>
            </div>
            <div className="d-flex align-items-center">
              <Button color="link" className="px-1">
                <i className="bi bi-chevron-left"></i>
              </Button>
              <Button color="link" className="px-1">
                <i className="bi bi-caret-left-fill"></i>
              </Button>
              <Button color="primary" size="sm" className="mx-1">
                1
              </Button>
              <Button color="link" className="px-1">
                <i className="bi bi-chevron-right"></i>
              </Button>
              <Button color="link" className="px-1">
                <i className="bi bi-caret-right-fill"></i>
              </Button>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default WorkspaceLanding;
