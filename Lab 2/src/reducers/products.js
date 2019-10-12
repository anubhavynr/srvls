import {
    REQUEST_ALL_PRODUCTS,
    RECEIVE_ALL_PRODUCTS,
    REQUEST_ADD_PRODUCT,
    RECEIVE_ADD_PRODUCT,
    REQUEST_EDIT_PRODUCT,
    RECEIVE_EDIT_PRODUCT,
    REQUEST_DELETE_PRODUCT,
    RECEIVE_DELETE_PRODUCT,
    REQUEST_PRODUCT_CATEGORIES,
    RECEIVE_PRODUCT_CATEGORIES,
} from '../actions';

const initialState = {
    products: [],
    productCount: 0,
    categories: [],
}

function addProduct(products, product) {
    const productsCopy = products.slice();
    productsCopy.push(product);
    
    return productsCopy;
}

function updateProduct(products, updatedProduct) {
    const productsCopy = products.slice();
    return productsCopy.map(product => product.id === updatedProduct.id ?  updatedProduct : product);
}

function deleteProduct(products, deletedProduct) {
    const productsCopy = products.slice();
    return productsCopy.filter(product => product.id !== deletedProduct.id);
}

export const products = (state = initialState, action) => {
    switch (action.type) {
        case REQUEST_PRODUCT_CATEGORIES:
            return null;
        case RECEIVE_PRODUCT_CATEGORIES:
            return {
                ...state,
                categories: action.categories,
            }
        case REQUEST_ALL_PRODUCTS:
            return null;
        case RECEIVE_ALL_PRODUCTS:
            return {
                ...state,
                products: action.products,
                productCount: action.products.length,
            }
        case REQUEST_ADD_PRODUCT:
                return null;
        case RECEIVE_ADD_PRODUCT: 
            const productsWithAdd = addProduct(state.products, action.product);

            return {
                ...state,
                products: productsWithAdd,
                productCount: productsWithAdd.length,
            }
        case REQUEST_EDIT_PRODUCT:
                return null;
        case RECEIVE_EDIT_PRODUCT: 
            const productsWithUpdate = updateProduct(state.products, action.product);

            return {
                ...state,
                products: productsWithUpdate,
                productCount: productsWithUpdate.length,
            }
        case REQUEST_DELETE_PRODUCT:
                return null;
        case RECEIVE_DELETE_PRODUCT: 
            const productsWithDelete = deleteProduct(state.products, action.product);

            return {
                ...state,
                products: productsWithDelete,
                productCount: productsWithDelete.length,
            }
        default:
            return state;
    }    
};
