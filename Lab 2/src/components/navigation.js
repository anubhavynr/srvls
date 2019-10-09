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
import NavDropdown from 'react-bootstrap/NavDropdown';
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
      <Navbar.Brand href="#home">Brand</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
          <Nav.Link href="/">Home</Nav.Link>
          <AuthenticatedLink isAuthenticated={currentUser.isAuthenticated} linkHref="dashboard" linkText="Dashboard" />
          <AuthenticatedLink isAuthenticated={currentUser.isAuthenticated} linkHref="products" linkText="Products" />
          <AuthenticatedLink isAuthenticated={currentUser.isAuthenticated} linkHref="orders" linkText="Orders" />
          <NavDropdown title="Functionality" id="functionality-dropdown">
            <NavDropdown.Item href="funcion/1">Function 1</NavDropdown.Item>
            <NavDropdown.Item href="function/2">Function 2</NavDropdown.Item>
            <NavDropdown.Item href="function/3">Function 3</NavDropdown.Item>
            <NavDropdown.Divider />
            <NavDropdown.Item href="#function/4">Function 4</NavDropdown.Item>
          </NavDropdown>
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
