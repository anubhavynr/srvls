import {
    RECEIVE_AUTHENTICATE_USER,
} from '../actionTypes';
import {
    closeModal,
} from '../../modals/actions';

export const receiveUserAuthentication = user => {
    return {
        type: RECEIVE_AUTHENTICATE_USER,
        user,
    };
};

export const authenticateUser = () => {
    return function(dispatch) {
        const user = {
            firstName: 'User',
            lastName: 'Mustermann',
            email: 'max@mustermann.com',
            isAuthenticated: true,
        }
            
        sessionStorage.setItem('isAuthenticated', 'true');

        dispatch(receiveUserAuthentication(user));
        dispatch(closeModal());
    };
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
