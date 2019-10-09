import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { fetchOrders } from '../actions';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Table from 'react-bootstrap/Table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faTimesCircle,
    faCheckCircle,
} from '@fortawesome/free-solid-svg-icons';

function Orders(props) {
    const {
        orders,
        fetchOrders,
     } = props;

    useEffect(() => {
        fetchOrders();
    }, [fetchOrders]);

    return (
        <Container>
            <h2>Orders</h2>
            <Container>
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
                                                <td>{orderDate}</td>
                                                <td>{totalItems}</td>
                                                <td>{purchaserId}</td>
                                                <td className="text-center">{shipDate === null ? <FontAwesomeIcon icon={faTimesCircle} /> : <FontAwesomeIcon icon={faCheckCircle} />}</td>
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

export default connect(mapStateToProps, { fetchOrders })(Orders);
