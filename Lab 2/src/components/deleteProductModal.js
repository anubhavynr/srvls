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
        params,
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
                    <h4>Are you sure you want to delete: <strong>{params.name}</strong>?</h4>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button variant="success" onClick={() => deleteProduct(params.id)}>
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
        params: state.modal.params,
        show: state.modal.currentModal !== null,
    };
}

export default connect(mapStateToProps, { closeModal, deleteProduct })(DeleteProductModal);
