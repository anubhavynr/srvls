import React from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
import bgImage from '../assets/green-02@2x.png';

function Home() {
    return (
        <Container style={{ backgroundImage: `url(${bgImage})`, backgroundSize: 'cover' }}>
            <Container fluid>
                <Row>
                    <Col>
                        <h1 className="display-2 text-light mb-2 mt-5"><strong>SVRLS Storefront</strong> </h1>
                        <p className="lead text-light mb-5">Serve more, manage less</p>
                        <p>
                            <Button href="#" variant='danger'>Get Started Now <FontAwesomeIcon icon={faArrowRight}/></Button>
                        </p>
                    </Col>
                </Row>
            </Container>
        </Container>
    )
}

export default Home;
