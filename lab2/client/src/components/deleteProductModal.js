import React from 'react';
import { connect } from 'react-redux';
import { closeModal, deleteProduct } from '../actions';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

function DeleteProductModal(props) {
    const {
        show,
        title = 'Delete Product',
        buttonText = 'Delete Product',
        product,
        closeModal,
        deleteProduct,
    } = props;

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
                    <h4>Are you sure you want to delete: <strong>{product.name}</strong>?</h4>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button variant="success" onClick={() => deleteProduct(product)}>
                        {buttonText}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

const mapDispatchToProps = {
    closeModal,
    deleteProduct,
};

const mapStateToProps = state => {
    return {
        currentModal: state.modal.currentModal,
        show: state.modal.currentModal !== null,
        product: state.modal.product,
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(DeleteProductModal);
