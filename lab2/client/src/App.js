import React from 'react';
import { BrowserRouter as Router, Route, Redirect } from 'react-router-dom';
import { Provider, connect } from 'react-redux';
import Container from 'react-bootstrap/Container';
import Jumbotron from 'react-bootstrap/Jumbotron';

import {
  Navigation,
  PrivateRoute,
} from './components/navigation';
import { Home } from './components/home';
import { Dashboard } from './components/dashboard';
import { Products } from './components/products';
import { Orders } from './components/orders';
import { Footer } from './components/layout';
import { ModalWrapper as Modal } from './components/modals';

const App = ({ store, currentUser }) => {
  const { isAuthenticated } = currentUser;

  return (
    <Provider store={store}>
      <Router>
        <Container>
          <Container>
            <Navigation />
          </Container>
          <Container>
            <Jumbotron>
              <Route exact={true} path='/'>
                 {isAuthenticated ? <Redirect to="/dashboard" /> : <Home /> }
              </Route>
              <PrivateRoute path='/dashboard' component={Dashboard} />
              <PrivateRoute path='/products' component={Products} />
              <PrivateRoute path='/orders' component={Orders} />
            </Jumbotron>
          </Container>
          <Container>
            <Footer />
          </Container>
        </Container>
        <Modal />
      </Router>
    </Provider>
  );
}

const mapStateToProps = state => ({
    currentUser: state.user,
});

export default connect(mapStateToProps)(App);
