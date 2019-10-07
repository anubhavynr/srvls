import React from 'react';
import { connect } from 'react-redux';
import { closeModal } from '../actions';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAward } from '@fortawesome/free-solid-svg-icons';

function SignUpModal(props) {
    const {
        show,
        title = 'SVRLS Sign Up!',
        buttonText = 'Sign Up',
        privacyMessage = 'Your email will never be shared.',
        closeModal,
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
                        <Form.Group controlId="signUpformFirstName">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control type="text" placeholder="FirstName" />
                        </Form.Group>
                        <Form.Group controlId="signUpformLastName">
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control type="text" placeholder="LastName" />
                        </Form.Group>
                        <Form.Group controlId="signUpformEmail">
                            <Form.Label>Email address</Form.Label>
                            <Form.Control type="email" placeholder="Enter email" />
                            <Form.Text className="text-muted">
                                {privacyMessage} <FontAwesomeIcon icon={faAward} />
                            </Form.Text>
                        </Form.Group>
                        <Form.Group controlId="signUpformCompany">
                            <Form.Label>Company</Form.Label>
                            <Form.Control type="text" placeholder="CompanyName" />
                        </Form.Group>
                        <Form.Group controlId="signUpformPlan">
                            <Form.Label>Plan</Form.Label>
                            <DropdownButton variant="primary" id="plan-dropdown-button" title="Plan">
                                <Dropdown.Item>Standard Tier</Dropdown.Item>
                                <Dropdown.Item>Professional Tier</Dropdown.Item>
                                <Dropdown.Item>Advanced Tier</Dropdown.Item>
                            </DropdownButton>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button variant="success" onClick={closeModal}>
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
    };
}

export default connect(mapStateToProps, { closeModal })(SignUpModal);
