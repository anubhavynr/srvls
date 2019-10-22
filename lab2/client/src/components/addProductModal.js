import React, { useState } from 'react';
import { connect } from 'react-redux';
import {
    closeModal,
    addProduct,
} from '../actions';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';

function AddProductModal(props) {
    const {
        show,
        title = 'Add Product',
        buttonText = 'Add Product',
        categories,
        addProduct,
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

    const { value: sku, bind: bindSku } = useInput('');
    const { value: name, bind: bindName } = useInput('');
    const { value: price, bind: bindPrice } = useInput('');
    const { value: categoryId, bind: bindCategory } = useInput('');

    const submitProductAdd = () => {
        const category = categories.find(category => category.id.toString() === categoryId.toString());
        const product = {
            sku,
            name,
            price,
            category,
        };

        addProduct(product);
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
                        <Form.Group controlId="productSKU">
                            <Form.Label>SKU</Form.Label>
                            <Form.Control type="text" placeholder="Enter SKU" {...bindSku} />
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
                            <Form.Control as="select" {...bindCategory}>
                                {
                                    categories.map(category => <option key={category.id} value={category.id}>{category.name}</option>)
                                }
                            </Form.Control>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button variant="success" onClick={submitProductAdd}>
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
        categories: state.products.categories,
    };
}

const mapDispatchToProps = {
    addProduct,
    closeModal,
};

export default connect(mapStateToProps, mapDispatchToProps)(AddProductModal);
