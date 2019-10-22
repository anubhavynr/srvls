import React from 'react';
import { connect } from 'react-redux';
import { closeModal } from '../actions';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAward } from '@fortawesome/free-solid-svg-icons';

function SignUpModal(props) {
    const {
        show,
        title = 'Sign Up',
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
                            <Form.Label>Email Address</Form.Label>
                            <Form.Control type="email" placeholder="EmailAddress" />
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
                            <Form.Control as="select">
                                <option key={1} value={1}>Standard Tier</option>
                                <option key={2} value={2}>Professional Tier</option>
                                <option key={3} value={3}>Advanced Tier</option>
                            </Form.Control>
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
