import { REQUEST_ALL_PRODUCTS, RECEIVE_ALL_PRODUCTS } from '../actions';

const initialState = {
    products: [],
    productCount: 0,
}

export const products = (state = initialState, action) => {
    switch (action.type) {
        case REQUEST_ALL_PRODUCTS:
            return null;
        case RECEIVE_ALL_PRODUCTS:
            return {
                ...state,
                products: action.products,
                productCount: action.products.length,
            }
        default:
            return state;
    }    
};
