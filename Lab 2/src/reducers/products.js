import { REQUEST_ALL_PRODUCTS, RECEIVE_ALL_PRODUCTS, REQUEST_DELETE_PRODUCT, RECEIVE_DELETE_PRODUCT } from '../actions';

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
        case REQUEST_DELETE_PRODUCT:
                return null;
        case RECEIVE_DELETE_PRODUCT: 
            const productsCopy = state.products.slice();

            const products = productsCopy.filter(product => product.id !== action.id);

            return {
                ...state,
                products,
                productCount: products.length,
            }
        default:
            return state;
    }    
};
