import React from 'react';
import { connect } from 'react-redux';
import { signUpModal } from '../../modals/actions';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
import bgImage from '../../../assets/green-02@2x.png';

function Home(props) {
    const { signUpModal } = props;

    return (
        <Container style={{ backgroundImage: `url(${bgImage})`, backgroundSize: 'cover' }}>
            <Container fluid style={{height: '40vh'}}>
                <Row>
                    <Col>
                        <h1 className="display-2 text-light"><strong>SaaS Commerce</strong> </h1>
                        <p className="lead text-light mb-5">Your SaaS commerce solution</p>
                        <p>
                            <Button variant='danger' onClick={signUpModal}>Get Started Now <FontAwesomeIcon icon={faArrowRight}/></Button>
                        </p>
                    </Col>
                </Row>
            </Container>
        </Container>
    )
}

export default connect(() => ({}), { signUpModal })(Home);
