import Axios from 'axios';
import config from '../../../shared/config';
import {
    RECEIVE_ALL_ORDERS,
    RECEIVE_ADD_ORDER,
    RECEIVE_EDIT_ORDER,
    RECEIVE_DELETE_ORDER,
} from '../actionTypes';
import { closeModal } from '../../modals/actions';

export const addOrderFinished = (order) => {
    return {
        type: RECEIVE_ADD_ORDER,
        order,
    };
};

export const editOrderFinished = (order) => {
    return {
        type: RECEIVE_EDIT_ORDER,
        order,
    };
};

export const deleteOrderFinished = (order) => {
    return {
        type: RECEIVE_DELETE_ORDER,
        order,
    };
};

export const receiveAllOrders = orders => {
    return {
        type: RECEIVE_ALL_ORDERS,
        orders,
    };
};

export const fetchOrders = () => {
    return function(dispatch) {
        const url = `${config.api.base_url}/orders`;
    
        Axios.get(url)
            .then(response => {
                dispatch(receiveAllOrders(response.data))
            }, error => console.error(error));
    };
};

export const addOrder = (order) => {
    return function(dispatch) {
        const url = `${config.api.base_url}/orders`;

        Axios.post(url, order)
            .then((response) => {
                dispatch(addOrderFinished(response.data));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    };
};

export const editOrder = (order) => {
    return function(dispatch) {
        const url = `${config.api.base_url}/orders/${order.id}`;

        Axios.put(url, order)
            .then((response) => {
                dispatch(editOrderFinished(response.data));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    }
};

export const deleteOrder = (order) => {
    return function(dispatch) {
        const url = `${config.api.base_url}/orders/${order.id}`;

        Axios.delete(url, { data: order })
            .then((response) => {
                dispatch(deleteOrderFinished(order));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    };
};
