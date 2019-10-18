import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import {
    fetchOrders,
    fetchProducts,
    addOrderModal,
    editOrderModal,
} from '../actions';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';
import Tooltip from 'react-bootstrap/Tooltip';
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import moment from 'moment';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faTimesCircle,
    faCheckCircle,
    faPlus,
    faEdit,
    faTrash,
} from '@fortawesome/free-solid-svg-icons';

function Orders(props) {
    const {
        orders,
        fetchOrders,
        fetchProducts,
        addOrderModal,
        editOrderModal,
     } = props;

    useEffect(() => {
        fetchOrders();
    }, [fetchOrders]);

    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    orders.sort((a, b) => b.id - a.id);

    return (
        <Container>
            <h2>Orders</h2>
            <Container>
                <Row className='mb-2'>
                    <Col>
                        <Button onClick={addOrderModal} variant='success' className="float-right"> Add Order <FontAwesomeIcon icon={faPlus} /></Button>
                    </Col>
                </Row>
                <Row>
                    <Table>
                        <thead>
                            <tr>
                                <th>Order Number</th>
                                <th>Order Total</th>
                                <th>Order Date</th>
                                <th>Total Items</th>
                                <th>Purchaser</th>
                                <th className="text-center">Shipped</th>
                                <th className="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                orders ?
                                    orders.map((order) => {
                                        const { id,
                                            orderDate,
                                            lineItems,
                                            total,
                                            totalItems = lineItems.length,
                                            purchaser,
                                            purchaserId = purchaser.id,
                                            shipDate,
                                        } = order;

                                        return (
                                            <tr key={id}>
                                                <td>{id}</td>
                                                <td>${total}</td>
                                                <td>{moment(orderDate).format('L')}</td>
                                                <td>{totalItems}</td>
                                                <td>{purchaserId}</td>
                                                <td className="text-center">{shipDate === null ? <FontAwesomeIcon className="text-danger" icon={faTimesCircle} /> : <OverlayTrigger placement="top" overlay={<Tooltip id="tooltip-ship-date">{shipDate}</Tooltip>}><FontAwesomeIcon className="text-success" icon={faCheckCircle} /></OverlayTrigger>}</td>
                                                <td className="text-center"><Button variant='secondary' onClick={() => editOrderModal(order)}> Edit <FontAwesomeIcon icon={faEdit} /></Button> <Button onClick={() => {}} variant='danger'> Del <FontAwesomeIcon icon={faTrash} /></Button></td>
                                            </tr>
                                        );
                                    }
                                    )
                                    :
                                    null
                            }
                        </tbody>
                    </Table>
                </Row>
            </Container>
        </Container>
    )
}

const mapStateToProps = state => {
    return {
        orders: state.orders.orders,
    };
};

const mapDispatchToProps = {
    fetchOrders,
    fetchProducts,
    addOrderModal,
    editOrderModal,
}

export default connect(mapStateToProps, mapDispatchToProps)(Orders);
