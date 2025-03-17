import React, { useState } from 'react';
import { Col, Row, Input, Button, Form, Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
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
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [selectedContentType, setSelectedContentType] = useState('pick one...');

  // Sample data for content items
  const contentItems = [
    { id: 1, title: 'AsciiDoc', type: 'slide', typeCode: 'P', color: 'blue' },
    { id: 2, title: 'Kotlin', type: 'site', typeCode: 'W', color: 'red' },
    { id: 3, title: 'Python', type: 'training', typeCode: 'T', color: 'purple' },
    { id: 4, title: 'Springboot', type: 'training', typeCode: 'T', color: 'purple' },
    { id: 5, title: 'FastAPI', type: 'training', typeCode: 'T', color: 'purple' },
    { id: 6, title: 'Gradle', type: 'article', typeCode: 'A', color: 'darkSlateGrey' },
  ];

  const contentTypes = [
    { label: 'Slide', value: 'slide' },
    { label: 'Site', value: 'site' },
    { label: 'Article', value: 'article' },
    { label: 'School', value: 'school' },
    { label: 'Training', value: 'training' },
  ];

  const toggle = () => setDropdownOpen(prevState => !prevState);

  const handleCreateNew = type => {
    setSelectedContentType(type);
    setDropdownOpen(false);
    // Ici, vous pouvez ajouter la logique pour créer un nouveau contenu en fonction du type sélectionné
    // console.log(`Creating new ${type}`);
  };

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
      {/* Top controls row with dropdown button positioned left */}
      <Row className="controls-bar">
        <Col xs={12}>
          <div className="d-flex justify-content-start align-items-center">
            <Dropdown isOpen={dropdownOpen} toggle={toggle} className="content-type-dropdown">
              <DropdownToggle caret color="primary" className="d-flex align-items-center">
                <i className="bi bi-plus-lg me-2"></i>
                New
              </DropdownToggle>
              <DropdownMenu>
                {contentTypes.map(type => (
                  <DropdownItem key={type.value} onClick={() => handleCreateNew(type.value)}>
                    <span style={{ color: typeColors[type.value], fontWeight: 'bold' }}>
                      ({typeCodes[type.value]}) {type.label}
                    </span>
                  </DropdownItem>
                ))}
              </DropdownMenu>
            </Dropdown>
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
        {/* Texte aligné à gauche */}
        <Col xs={12} md={6} className="pagination-info">
          <span>1 - 10 of 10 items</span>
        </Col>

        {/* Pagination centrée */}
        <Col xs={12} md={6} className="d-flex justify-content-center">
          <div className="pagination-controls">
            <Button color="link">
              <i className="bi bi-chevron-double-left"></i>
            </Button>
            <Button color="link">
              <i className="bi bi-chevron-left"></i>
            </Button>
            <span className="pagination-number">1</span>
            <Button color="link">
              <i className="bi bi-chevron-right"></i>
            </Button>
            <Button color="link">
              <i className="bi bi-chevron-double-right"></i>
            </Button>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default WorkspaceLanding;
