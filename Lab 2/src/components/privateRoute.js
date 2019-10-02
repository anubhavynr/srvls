import React from 'react';
import { Redirect, Route } from 'react-router-dom';
import { connect } from 'react-redux';

const PrivateRoute = ({ component: Component, currentUser, ...rest }) => (
    <Route { ...rest } render={props => (
        currentUser.isAuthenticated
            ? <Component { ...props } />
            : <Redirect to='/' />
    )} />
);

const mapStateToProps = state => ({
    currentUser: state.user,
});

export default connect(mapStateToProps)(PrivateRoute);
