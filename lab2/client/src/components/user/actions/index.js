import Axios from 'axios';
import config from '../../../shared/config';
import {
    RECEIVE_AUTHENTICATE_USER,
    RECEIVE_REGISTER_USER,
} from '../actionTypes';
import {
    closeModal,
} from '../../modals/actions';

const registerUserFinished = user => {
    return {
        type: RECEIVE_REGISTER_USER,
        user,
    };
};

export const receiveUserAuthentication = user => {
    return {
        type: RECEIVE_AUTHENTICATE_USER,
        user,
    };
};

export const authenticateUser = (userChallenge) => {
    return function(dispatch) {
        let user;

        if(userChallenge.userName === config.user.userName && userChallenge.password === config.user.password) {
            user = {
                firstName: 'User',
                lastName: 'Mustermann',
                email: 'max@mustermann.com',
                isAuthenticated: true,
            }

            sessionStorage.setItem('isAuthenticated', 'true');
        }

        dispatch(receiveUserAuthentication(user));
        dispatch(closeModal());
    };
};

export const registerUser = (user) => {
    return function(dispatch) {
        const url = `${config.api.base_url}/registration`;

        Axios.post(url, user)
            .then(response => {
                dispatch(registerUserFinished(response.data));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    }
};

export const signOutUser = () => {
    return function(dispatch) {     
        sessionStorage.removeItem('isAuthenticated');

        const user = {
            isAuthenticated: false,
        };

        dispatch(receiveUserAuthentication(user));
    };
};
