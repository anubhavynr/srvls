import {
    REQUEST_AUTHENTICATE_USER,
    RECEIVE_AUTHENTICATE_USER,
} from '../actionTypes';

const initialState = {
    firstName: 'User',
    lastName: 'Mustermann',
    isAuthenticated: sessionStorage.getItem('isAuthenticated') === 'true' ? true : false,
    tenantId: null,
};

export const user = (state = initialState, action) => {
    switch (action.type) {
        case REQUEST_AUTHENTICATE_USER:
            return null;
        case RECEIVE_AUTHENTICATE_USER:
            return {
                ...state,
                ...action.user,
            }
        default:
            return state;
    };
};
