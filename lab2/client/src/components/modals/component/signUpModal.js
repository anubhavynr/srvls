/**
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
import React, { useState } from 'react';
import { connect } from 'react-redux';
import { closeModal } from '../actions';
import { registerUser } from '../../user/actions';
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
        registerUser,
    } = props;

    const [validated, setValidated] = useState(false);

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
    const { value: email, bind: bindEmail } = useInput('');
    const { value: password, bind: bindPassword } = useInput('');
    const { value: company, bind: bindCompany } = useInput('');
    const { value: plan, bind: bindPlan } = useInput('');

    const submitRegisterUser = event => {
        const form = event.currentTarget;
        const user = {
            firstName,
            lastName,
            email,
            password,
            company,
            plan,
        };

        event.preventDefault();

        if(form.checkValidity() === false) {
            event.stopPropagation();
        } else {
            setValidated(true);
            registerUser(user);
        }
    };

    return (
        <>
            <Modal
                show={show}
                onHide={closeModal}
                centered
            >
            <Form id="signUpForm" noValidate validated={validated} onSubmit={submitRegisterUser}>
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                        <Form.Group controlId="signUpformFirstName">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control type="text" placeholder="FirstName" required {...bindFirstName} />
                        </Form.Group>
                        <Form.Group controlId="signUpformLastName">
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control type="text" placeholder="LastName" required {...bindLastName} />
                        </Form.Group>
                        <Form.Group controlId="signUpformEmail">
                            <Form.Label>Email Address</Form.Label>
                            <Form.Control type="email" placeholder="EmailAddress" required {...bindEmail} />
                            <Form.Text className="text-muted">
                                {privacyMessage} <FontAwesomeIcon icon={faAward} />
                            </Form.Text>
                        </Form.Group>
                        <Form.Group controlId="signUpformPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Password" required pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])\S{8,}" {...bindPassword} />
                            <Form.Control.Feedback type="invalid">
                                Your password must be at least 8 characters long and contain one uppercase letter, one lowercase letter, and a number.
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group controlId="signUpformCompany">
                            <Form.Label>Company</Form.Label>
                            <Form.Control type="text" placeholder="CompanyName" required {...bindCompany} />
                        </Form.Group>
                        <Form.Group controlId="signUpformPlan">
                            <Form.Label>Plan</Form.Label>
                            <Form.Control as="select" {...bindPlan} >
                                <option key={1} value={1}>Standard Tier</option>
                                <option key={2} value={2}>Professional Tier</option>
                                <option key={3} value={3}>Advanced Tier</option>
                            </Form.Control>
                        </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button type="submit" variant="success">
                        {buttonText}
                    </Button>
                </Modal.Footer>
            </Form>
            </Modal>
        </>
    );
}

const mapDispatchToProps = {
    closeModal,
    registerUser,
};

const mapStateToProps = state => {
    return {
        currentModal: state.modal.currentModal,
        show: state.modal.currentModal !== null,
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(SignUpModal);
