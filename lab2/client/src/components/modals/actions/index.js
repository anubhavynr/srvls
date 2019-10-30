import {
    SET_CURRENT_MODAL,
    SIGN_IN_MODAL,
    SIGN_UP_MODAL,
    ADD_PRODUCT_MODAL,
    EDIT_PRODUCT_MODAL,
    DELETE_PRODUCT_MODAL,
    ADD_ORDER_MODAL,
    EDIT_ORDER_MODAL,
    DELETE_ORDER_MODAL,
    CLOSE_MODAL,
} from '../actionTypes';

export const signInModal = () => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: SIGN_IN_MODAL,
    };
};

export const signUpModal = () => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: SIGN_UP_MODAL,
    };
};

export const addProductModal = () => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: ADD_PRODUCT_MODAL,
    };
};

export const editProductModal = (product) => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: EDIT_PRODUCT_MODAL,
        product,
    };
};

export const deleteProductModal = (product) => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: DELETE_PRODUCT_MODAL,
        product,
    };
};

export const addOrderModal = () => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: ADD_ORDER_MODAL,
    };
};

export const editOrderModal = (order) => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: EDIT_ORDER_MODAL,
        order,
    };
};

export const deleteOrderModal = (order) => {
    return {
        type: SET_CURRENT_MODAL,
        currentModal: DELETE_ORDER_MODAL,
        order,
    };
};

export const closeModal = () => {
    return {
        type: CLOSE_MODAL,
    };
};
