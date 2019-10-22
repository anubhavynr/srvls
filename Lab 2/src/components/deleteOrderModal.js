import React from 'react';
import { connect } from 'react-redux';
import {
    closeModal,
    deleteOrder,
 } from '../actions';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

function DeleteOrderModal(props) {
    const {
        show,
        title = 'Delete Order',
        buttonText = 'Delete Order',
        order,
        closeModal,
        deleteOrder,
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
                    <h4>Are you sure you want to delete order: <strong>{order.id}</strong>?</h4>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button variant="success" onClick={() => deleteOrder(order)}>
                        {buttonText}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

const mapDispatchToProps = {
    closeModal,
    deleteOrder,
};

const mapStateToProps = state => {
    return {
        currentModal: state.modal.currentModal,
        show: state.modal.currentModal !== null,
        order: state.modal.order,
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(DeleteOrderModal);
