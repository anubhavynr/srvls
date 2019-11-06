import React, { useState } from 'react';
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

    const { value: email, bind: bindEmail } = useInput('');
    const { value: password, bind: bindPassword } = useInput('');

    function singInUser() {
        const userChallenge = {
            userName: email,
            password: password,
        }

        authenticateUser(userChallenge);
    }

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
                            <Form.Control type="email" placeholder="Enter email" {...bindEmail} />
                        </Form.Group>
                        <Form.Group controlId="signInformPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Password" {...bindPassword} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal} >
                        Close
                    </Button>
                    <Button variant="success" onClick={singInUser} >
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
