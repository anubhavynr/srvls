import React from 'react';
import { connect } from 'react-redux';
import { closeModal } from '../actions';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import {
    authenticateUser,
} from '../../user/actions';

function SignInModal(props) {
    const {
        show,
        title = 'Sign In',
        buttonText = 'Sign In',
        closeModal,
        authenticateUser,
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
                    <Form>
                        <Form.Group controlId="signInformEmail">
                            <Form.Label>Email address</Form.Label>
                            <Form.Control type="email" placeholder="Enter email" />
                        </Form.Group>
                        <Form.Group controlId="signInformPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Password" />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal} >
                        Close
                    </Button>
                    <Button variant="success" onClick={authenticateUser} >
                        {buttonText}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

const mapDispatchToProps = {
  closeModal,
  authenticateUser,  
};

const mapStateToProps = state => {
    return {
        currentModal: state.modal.currentModal,
        show: state.modal.currentModal !== null,
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(SignInModal);
