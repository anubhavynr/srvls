import React, { useState } from 'react';
import { connect } from 'react-redux';
import {
    closeModal,
    addOrder,
} from '../actions';
import Col from 'react-bootstrap/Col';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import moment from 'moment';

function AddOrderModal(props) {
    const {
        show,
        title = 'Add Order',
        buttonText = 'Add Order',
        products,
        addOrder,
        closeModal,
    } = props;

    const useInput = initialValue => {
        const [value, setValue] = useState(initialValue);

        return {
            value,
            setValue,
            reset: () => setValue(''),
            bind: {
                value,
                onChange: event => {
                    setValue(event.target.value)
                }
            },
        };
    };

    const { value: firstName, bind: bindFirstName } = useInput('');
    const { value: lastName, bind: bindLastName } = useInput('');
    const { value: address1, bind: bindAddress1 } = useInput('');
    const { value: address2, bind: bindAddress2 } = useInput('');
    const { value: city, bind: bindCity } = useInput('');
    const { value: state, bind: bindState } = useInput('');
    const { value: zipCode, bind: bindZipCode } = useInput('');
    const { value: product1, bind: bindProduct1 } = useInput('');
    const { value: quantity1, bind: bindQuantity1 } = useInput('');
    const { value: product2, bind: bindProduct2 } = useInput('');
    const { value: quantity2, bind: bindQuantity2 } = useInput('');
    const { value: product3, bind: bindProduct3 } = useInput('');
    const { value: quantity3, bind: bindQuantity3 } = useInput('');

    const submitOrderAdd = () => {
        const address = {
            line1: address1,
            line2: address2,
            city: city,
            state: state,
            postalCode: zipCode,
        };
        const lineItems = [];

        if(quantity1) {
            lineItems.push(
                {
                    product: {
                        id: products[0].id,
                        sku: products[0].sku,
                        name: products[0].name,
                        price: products[0].price,
                        category: {
                            id: products[0].category.id,
                            name: products[0].category.name,
                        }
                    },
                    quantity: quantity1,
                    unitPurchasePrice: products[0].price,
                    extendedPurchasePrice: products[0].price * quantity1,
                }
            );
        }

        if(quantity2) {
            lineItems.push(
                {
                    product: {
                        id: products[1].id,
                        sku: products[1].sku,
                        name: products[1].name,
                        price: products[1].price,
                        category: {
                            id: products[1].category.id,
                            name: products[1].category.name,
                        }
                    },
                    quantity: quantity2,
                    unitPurchasePrice: products[1].price,
                    extendedPurchasePrice: products[1].price * quantity2,
                }
            );
        }

        if(quantity3) {
            lineItems.push(
                {
                    product: {
                        id: products[2].id,
                        sku: products[2].sku,
                        name: products[2].name,
                        price: products[2].price,
                        category: {
                            id: products[2].category.id,
                            name: products[2].category.name,
                        }
                    },
                    quantity: quantity3,
                    unitPurchasePrice: products[2].price,
                    extendedPurchasePrice: products[2].price * quantity3,
                }
            );
        }

        const order = {
            orderDate: moment().format('YYYY-MM-DD').toString(),
            purchaser: {
                firstName,
                lastName,
                id: 1,
            },
            shipAddress: address,
            billAddress: address,
            lineItems,
            total: lineItems.reduce((acc, cur) => acc + cur.extendedPurchasePrice, 0).toFixed(2),
        };

        addOrder(order);
    };

    return (
        <>
            <Modal
                show={show}
                onHide={closeModal}
                backdrop='static'
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <h4>Purchaser</h4>
                        <Form.Row>
                            <Form.Group as={Col} controlId="purchaserFirstName">
                                <Form.Label>First Name</Form.Label>
                                <Form.Control type="text" placeholder="Enter First Name" {...bindFirstName} />
                            </Form.Group>
                            <Form.Group as={Col} controlId="purchaserLastName">
                                <Form.Label>Last Name</Form.Label>
                                <Form.Control type="text" placeholder="Enter Last Name" {...bindLastName} />
                            </Form.Group>
                        </Form.Row>
                        <h4>Shipping Information</h4>
                        <Form.Row>
                            <Form.Group as={Col} controlId="shippingAddress1">
                                <Form.Label>Address</Form.Label>
                                <Form.Control type="text" placeholder="Enter Address" {...bindAddress1} />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="shippingAddress2">
                                <Form.Label>Address 2</Form.Label>
                                <Form.Control type="text" placeholder="Enter Address 2" {...bindAddress2} />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="shippingAddressCity">
                                <Form.Label>City</Form.Label>
                                <Form.Control type="text" placeholder="Enter City" {...bindCity} />
                            </Form.Group>
                            <Form.Group as={Col} controlId="shippingAddressState">
                                <Form.Label>State</Form.Label>
                                <Form.Control as="select" {...bindState}>
                                    {
                                        ['CA', 'GA', 'NV', 'NY', 'OR', 'TX', 'FL', 'WA'].map(state => <option key={state} value={state}>{state}</option>)
                                    }
                                </Form.Control>
                            </Form.Group>
                            <Form.Group as={Col} controlId="shippingZipCode">
                                <Form.Label>Zip Code</Form.Label>
                                <Form.Control type="text" placeholder="Enter Zip Code" {...bindZipCode} />
                            </Form.Group>
                        </Form.Row>
                        <h4>Products</h4>
                            {
                                products[0] ?
                                    (
                                        <Form.Row>
                                            <Form.Group as={Col} md={{offset: 1}} controlId="product1Check">
                                                <Form.Check label={`${products[0].name} (${products[0].sku})`} {...bindProduct1} />
                                            </Form.Group>
                                            <Form.Group as={Col} controlId="productQuantity1">
                                                <Form.Control type="text" placeholder="Enter Quantity" {...bindQuantity1} />
                                            </Form.Group>
                                        </Form.Row>
                                    ) : 
                                    null
                            }
                            {
                                products[1] ?
                                (
                                    <Form.Row>
                                        <Form.Group as={Col} md={{offset: 1}} controlId="product2Check">
                                            <Form.Check label={`${products[1].name} (${products[1].sku})`} {...bindProduct2} />
                                        </Form.Group>
                                        <Form.Group as={Col} controlId="productQuantity2">
                                            <Form.Control type="text" placeholder="Enter Quantity" {...bindQuantity2} />
                                        </Form.Group>
                                    </Form.Row>
                                ) : 
                                null
                            }
                            {
                                products[2] ?
                                (
                                    <Form.Row>
                                        <Form.Group as={Col} md={{offset: 1}} controlId="product13Check">
                                            <Form.Check label={`${products[2].name} (${products[2].sku})`} {...bindProduct3} />
                                        </Form.Group>
                                        <Form.Group as={Col} controlId="productQuantity3">
                                            <Form.Control type="text" placeholder="Enter Quantity" {...bindQuantity3} />
                                        </Form.Group>
                                    </Form.Row>
                                ) : 
                                null
                            }
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button variant="success" onClick={submitOrderAdd}>
                        {buttonText}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

const mapStateToProps = state => {
    return {
        currentModal: state.modal.currentModal,
        show: state.modal.currentModal !== null,
        products: state.products.products,
    };
}

const mapDispatchToProps = {
    addOrder,
    closeModal,
};

export default connect(mapStateToProps, mapDispatchToProps)(AddOrderModal);
           