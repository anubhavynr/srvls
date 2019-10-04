import React, { useState } from 'react';
import { connect } from 'react-redux';
import { editProduct, closeModal } from '../actions';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Dropdown from 'react-bootstrap/Dropdown';

function EditProductModal(props) {
    const {
        show,
        product,
        title = product ? `Edit ${product.name}` : 'Edit Product',
        buttonText = 'Edit Product',
        editProduct,
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

    const { value: sku, bind: bindSku } = useInput(product.sku);
    const { value: name, bind: bindName } = useInput(product.name);
    const { value: price, bind: bindPrice } = useInput(product.price);

    const submitProductUpdate = () => {
        const updatedProduct = {
            id: product.id,
            sku,
            name,
            price,
            category: product.category,
        };

        editProduct(updatedProduct);
    };
    
    return (
        <>
            <Modal
                show={show}
                onHide={closeModal}
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="productSKU">
                            <Form.Label>SKU</Form.Label>
                            <Form.Control type="text" placeholder="Enter SKU" value={product.sku} {...bindSku} />
                        </Form.Group>
                        <Form.Group controlId="productName">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text" placeholder="Enter product name" {...bindName} />
                        </Form.Group>
                        <Form.Group controlId="productName">
                            <Form.Label>Price</Form.Label>
                            <Form.Control type="text" placeholder="Enter product price" {...bindPrice} />
                        </Form.Group>
                        <Form.Group controlId="productCategory">
                            <Form.Label>Category</Form.Label>
                            <Dropdown>
                                <Dropdown.Toggle variant="primary" id="dropdown-basic">
                                    Category
                                </Dropdown.Toggle>
                                <Dropdown.Menu>
                                    <Dropdown.Item href="#/action-1">Category 1</Dropdown.Item>
                                    <Dropdown.Item href="#/action-2">Category 2</Dropdown.Item>
                                    <Dropdown.Item href="#/action-3">Category 3</Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button variant="success" onClick={submitProductUpdate}>
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
        product: state.modal.product,
    };
}

export default connect(mapStateToProps, { closeModal, editProduct })(EditProductModal);
