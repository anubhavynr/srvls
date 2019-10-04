import {
    REQUEST_ALL_PRODUCTS,
    RECEIVE_ALL_PRODUCTS,
    REQUEST_DELETE_PRODUCT,
    RECEIVE_DELETE_PRODUCT,
    REQUEST_EDIT_PRODUCT,
    RECEIVE_EDIT_PRODUCT,
} from '../actions';

const initialState = {
    products: [],
    productCount: 0,
}

function updateProduct(products, updatedProduct) {
    const productsCopy = products.slice();
    return productsCopy.map(product => product.id === updatedProduct.id ?  updatedProduct : product);
}

function deleteProduct(products, productId) {
    const productsCopy = products.slice();
    return productsCopy.filter(product => product.id !== productId);
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
            const deletedProducts = deleteProduct(state.products, action.id);

            return {
                ...state,
                products: deletedProducts,
                productCount: deletedProducts.length,
            }
        case REQUEST_EDIT_PRODUCT:
                return null;
        case RECEIVE_EDIT_PRODUCT: 
            const updatedProducts = updateProduct(state.products, action.product);

            return {
                ...state,
                products: updatedProducts,
                productCount: updatedProducts.length,
            }
        default:
            return state;
    }    
};
