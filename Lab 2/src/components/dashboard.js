import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { fetchProducts } from '../actions';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowCircleRight, faChartLine, faLongArrowAltRight, faLongArrowAltUp, faLongArrowAltDown, faListOl, faTags } from '@fortawesome/free-solid-svg-icons'

function Dashboard(props) {
    const { productCount,
        orderCount,
        fetchProducts,
    } = props;
    
    useEffect(() => {
            fetchProducts();
    }, [fetchProducts]);

    //REMOVE ME
    const trend = [faLongArrowAltRight, faLongArrowAltUp, faLongArrowAltDown][Math.floor(Math.random() * 3)];

    return (
        <Container>
            <Row>
                <h2>Tenant Name</h2>
            </Row>
            <Row>
                <Col>
                    <Card bg="light" text="black" border="success" style={{ width: '18rem' }}>
                        <Card.Body>
                            <Row>
                                <Col>
                                    <FontAwesomeIcon icon={faListOl} size="5x" />
                                </Col>
                                <Col>
                                    <p>Orders: { orderCount }</p>
                                </Col>
                            </Row>
                        </Card.Body>
                        <Card.Footer bg="gray">
                            <Row>
                                <Col>
                                    <Link style={{ textDecoration: 'none' }} to="/orders">Details <FontAwesomeIcon icon={faArrowCircleRight} /></Link>
                                </Col>
                            </Row>
                        </Card.Footer>
                    </Card>
                </Col>
                <Col>
                    <Card bg="light" text="black" border="success" style={{ width: '18rem' }}>
                        <Card.Body>
                            <Row>
                                <Col>
                                    <FontAwesomeIcon icon={faTags} size="5x" />
                                </Col>
                                <Col>
                                    <p>Products: { productCount }</p>
                                </Col>
                            </Row>
                        </Card.Body>
                        <Card.Footer bg="gray">
                            <Row>
                                <Col>
                                <Link style={{ textDecoration: 'none' }} to="/products">Details <FontAwesomeIcon icon={faArrowCircleRight} /></Link>
                                </Col>
                            </Row>
                        </Card.Footer>
                    </Card>
                </Col>
                <Col>
                    <Card bg="light" text="black" border="success" style={{ width: '18rem' }}>
                        <Card.Body>
                            <Row>
                                <Col>
                                    <FontAwesomeIcon icon={faChartLine} size="5x" />
                                </Col>
                                <Col>
                                    <p>Trend: <FontAwesomeIcon icon={trend} /></p>
                                </Col>
                            </Row>
                        </Card.Body>
                        <Card.Footer bg="gray">
                            <Row>
                                <Col>
                                <Link style={{ textDecoration: 'none' }} to="/dashboard">Details <FontAwesomeIcon icon={faArrowCircleRight} /></Link>
                                </Col>
                            </Row>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        </Container>
    )
}

const mapStateToProps = state => {
    return {
        productCount: state.products.productCount,
        orderCount: state.orders ? state.orders.orderCount : 0,
    }
}

export default connect(mapStateToProps, { fetchProducts })(Dashboard);
