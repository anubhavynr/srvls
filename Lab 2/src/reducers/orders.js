import {
    REQUEST_ALL_ORDERS,
    RECEIVE_ALL_ORDERS,
} from '../actions';

const initialState = {
    orders: [],
    orderCount: 0,
}

export const orders = (state = initialState, action) => {
    switch (action.type) {
        case REQUEST_ALL_ORDERS:
            return null;
        case RECEIVE_ALL_ORDERS:
            return {
                ...state,
                orders: action.orders,
                orderCount: action.orders.length,
            }
        default:
            return state;
    }    
};
