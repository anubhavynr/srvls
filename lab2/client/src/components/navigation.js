import React from 'react';
import { connect } from 'react-redux';
import {
  signInModal,
  signUpModal,
  signOutUser,
} from '../actions';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import AuthenticatedLink from './authenticatedLink';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';

const Navigation = (props) => {
  const {
    signInModal,
    signUpModal,
    signOutUser,
    currentUser, } = props;

  return (
    <Navbar expand="lg">
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
          <Nav.Link className="text-success" href="/">Home</Nav.Link>
          <AuthenticatedLink isAuthenticated={currentUser.isAuthenticated} linkHref="dashboard" linkText="Dashboard" />
          <AuthenticatedLink isAuthenticated={currentUser.isAuthenticated} linkHref="products" linkText="Products" />
          <AuthenticatedLink isAuthenticated={currentUser.isAuthenticated} linkHref="orders" linkText="Orders" />
        </Nav>
        {currentUser.isAuthenticated ? (
          <Form inline>
              <span>Welcome back, {currentUser.firstName}!</span>
              <Button variant="link" onClick={signOutUser}>Sign Out</Button>
          </Form>
        ) :
        <Form inline>
              <Button variant="link" onClick={signInModal}>Sign In</Button>
              <Button variant="success" onClick={signUpModal}>Sign Up</Button>
            </Form>
        }
      </Navbar.Collapse>
    </Navbar>
  );
}

const mapDispatchToProps = {
  signInModal,
  signUpModal,
  signOutUser,
};

const mapStateToProps = state => ({
  currentUser: state.user,
});

export default connect(mapStateToProps, mapDispatchToProps)(Navigation);
