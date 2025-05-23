import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { WorkspaceLanding } from './workspaceLanding';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  return (
    <span>
      {account?.login ? (
        <Row>
          <Col md="12">
            <div>
              <WorkspaceLanding />
            </div>
          </Col>
        </Row>
      ) : (
        <Row>
          <Col md="3" className="pad">
            <span className="hipster rounded" />
          </Col>
          <Col md="9">
            <div>
              <h1 className="display-12">
                <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
              </h1>
              <p className="lead">
                <Translate contentKey="home.subtitle">This is your homepage</Translate>
              </p>
              <Alert color="warning">
                <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>
                <Link to="/login" className="alert-link">
                  <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
                </Link>
              </Alert>
              <Alert color="warning">
                <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
                <Link to="/account/register" className="alert-link">
                  <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                </Link>
              </Alert>
            </div>
          </Col>
        </Row>
      )}
    </span>
  );
};

export default Home;
