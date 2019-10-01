import React from 'react';
import PropTypes from 'prop-types';
import Nav from 'react-bootstrap/Nav';

const AuthenticatedLink = (props) => {
  const {
    isAuthenticated = false,
    linkHref,
    linkText,
} = props;

  return isAuthenticated ? <Nav.Link href={linkHref}>{linkText}</Nav.Link> : null;
}

AuthenticatedLink.propTypes = {
    isAuthenticated: PropTypes.bool.isRequired,
    linkHref: PropTypes.string.isRequired,
    linkText: PropTypes.string.isRequired,
};

export default AuthenticatedLink;
