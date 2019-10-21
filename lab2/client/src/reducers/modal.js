import {
    SET_CURRENT_MODAL,
    CLOSE_MODAL,
    EDIT_PRODUCT_MODAL,
    DELETE_PRODUCT_MODAL,
    EDIT_ORDER_MODAL,
    DELETE_ORDER_MODAL,
} from '../actions';

const initialState = {
    currentModal: null,
    params: null,
}

export const modal = (state = initialState, action) => {
    switch (action.type) {
        case SET_CURRENT_MODAL:
            switch(action.currentModal) {
                case EDIT_PRODUCT_MODAL:
                    return {
                        ...state,
                        currentModal: EDIT_PRODUCT_MODAL,
                        product: action.product,
                    };
                case DELETE_PRODUCT_MODAL:
                        return {
                            ...state,
                            currentModal: DELETE_PRODUCT_MODAL,
                            product: action.product,
                        };
                case EDIT_ORDER_MODAL:
                    return {
                        ...state,
                        currentModal: EDIT_ORDER_MODAL,
                        order: action.order,
                    };
                case DELETE_ORDER_MODAL:
                    return {
                        ...state,
                        currentModal: DELETE_ORDER_MODAL,
                        order: action.order,
                    };
                default: 
                    return {
                        ...state,
                        currentModal: action.currentModal,
                    };
                }
        case CLOSE_MODAL: 
            return {
                ...state,
                currentModal: null,
            };
        default:
            return state;
    };    
};
