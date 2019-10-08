import Axios from 'axios';
import config from '../shared/config';

export const SET_CURRENT_MODAL = 'SET_CURRENT_MODAL';
export const SIGN_IN_MODAL = 'SIGN_IN_MODAL';
export const SIGN_UP_MODAL = 'SIGN_UP_MODAL';
export const ADD_PRODUCT_MODAL = 'ADD_PRODUCT_MODAL';
export const DELETE_PRODUCT_MODAL = 'DELETE_PRODUCT_MODAL';
export const EDIT_PRODUCT_MODAL = 'EDIT_PRODUCT_MODAL';
export const CLOSE_MODAL = 'CLOSE_MODAL';
export const REQUEST_ALL_PRODUCTS = 'REQUEST_ALL_PRODUCTS';
export const RECEIVE_ALL_PRODUCTS = 'RECEIVE_ALL_PRODUCTS';
export const REQUEST_PRODUCT_CATEGORIES = 'REQUEST_PRODUCT_CATEGORIES';
export const RECEIVE_PRODUCT_CATEGORIES = 'RECEIVE_PRODUCT_CATEGORIES';
export const REQUEST_ADD_PRODUCT = 'REQUEST_ALL_PRODUCTS';
export const RECEIVE_ADD_PRODUCT = 'REQUEST_ADD_PRODUCT';
export const REQUEST_DELETE_PRODUCT = 'REQUEST_DELETE_PRODUCT';
export const RECEIVE_DELETE_PRODUCT = 'RECEIVE_DELETE_PRODUCT';
export const REQUEST_EDIT_PRODUCT = 'REQUEST_DELETE_PRODUCT';
export const RECEIVE_EDIT_PRODUCT = 'RECEIVE_EDIT_PRODUCT';
export const ADD_PRODUCT_SUCCESS = 'REQUEST_ALL_PRODUCTS';

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

export const closeModal = () => {
    return {
        type: CLOSE_MODAL,
    };
};

export const requestAllProducts = () => {
    return {
        type: REQUEST_ALL_PRODUCTS,
    }
}

export const receiveAllProducts = products => {
    return {
        type: RECEIVE_ALL_PRODUCTS,
        products,
    }
}

export const receiveProductCategories = categories => {
    return {
        type: RECEIVE_PRODUCT_CATEGORIES,
        categories,
    }
}

export const addProductFinished = (product) => {
    return {
        type: RECEIVE_ADD_PRODUCT,
        product,
    }
};

export const editProductFinished = (product) => {
    return {
        type: RECEIVE_EDIT_PRODUCT,
        product,
    }
};

export const deleteProductFinished = (product) => {
    return {
        type: RECEIVE_DELETE_PRODUCT,
        product,
    }
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
    }
}

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
    }
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

        Axios.delete(url, product)
            .then(() => {
                dispatch(deleteProductFinished(product));
            }, error => console.error(error))
            .then(() => {
                dispatch(closeModal());
            }, error => console.error(error));
    }
};
