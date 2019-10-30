import Axios from 'axios';
import config from '../../../shared/config';
import {
    REQUEST_ALL_PRODUCTS,
    RECEIVE_ALL_PRODUCTS,
    RECEIVE_PRODUCT_CATEGORIES,
    RECEIVE_ADD_PRODUCT,
    RECEIVE_EDIT_PRODUCT,
    RECEIVE_DELETE_PRODUCT,
 } from '../actionTypes';
import { closeModal } from '../../modals/actions';

export const requestAllProducts = () => {
    return {
        type: REQUEST_ALL_PRODUCTS,
    };
};

export const receiveAllProducts = products => {
    return {
        type: RECEIVE_ALL_PRODUCTS,
        products,
    };
};

export const receiveProductCategories = categories => {
    return {
        type: RECEIVE_PRODUCT_CATEGORIES,
        categories,
    };
};

export const addProductFinished = (product) => {
    return {
        type: RECEIVE_ADD_PRODUCT,
        product,
    };
};

export const editProductFinished = (product) => {
    return {
        type: RECEIVE_EDIT_PRODUCT,
        product,
    };
};

export const deleteProductFinished = (product) => {
    return {
        type: RECEIVE_DELETE_PRODUCT,
        product,
    };
};

export const fetchProducts = () => {
    return function(dispatch) {
        const url = `${config.api.base_url}/products`;
    
        Axios.get(url)
            .then(response => {
                dispatch(receiveAllProducts(response.data))
            }, error => console.error(error));
    };
};

export const fetchProductCategories = () => {
    return function(dispatch) {
        const url = `${config.api.base_url}/categories`;

        Axios.get(url)
        .then(response => {
            dispatch(receiveProductCategories(response.data))
        }, error => console.error(error));
    };
};

export const addProduct = (product) => {
    return function(dispatch) {
        const url = `${config.api.base_url}/products`;

        Axios.post(url, product)
            .then((response) => {
                dispatch(addProductFinished(response.data));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    };
};

export const editProduct = (product) => {
    return function(dispatch) {
        const url = `${config.api.base_url}/products/${product.id}`;

        Axios.put(url, product)
            .then(() => {
                dispatch(editProductFinished(product));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    }
};

export const deleteProduct = (product) => {
    return function(dispatch) {
        const url = `${config.api.base_url}/products/${product.id}`;

        Axios.delete(url, { data: product })
            .then(() => {
                dispatch(deleteProductFinished(product));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    };
};
