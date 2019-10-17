import {
    SET_CURRENT_MODAL,
    CLOSE_MODAL,
    EDIT_PRODUCT_MODAL,
    EDIT_ORDER_MODAL,
} from '../actions';

const initialState = {
    currentModal: null,
    params: null,
}

export const modal = (state = initialState, action) => {
    switch (action.type) {
        case SET_CURRENT_MODAL: 
            if(action.currentModal === EDIT_PRODUCT_MODAL) {
                    return {
                        ...state,
                        currentModal: EDIT_PRODUCT_MODAL,
                        product: action.product,
                    };
                }

            if(action.currentModal === EDIT_ORDER_MODAL) {
                return {
                    ...state,
                    currentModal: EDIT_ORDER_MODAL,
                    order: action.order,
                };
            }
            
            return {
                ...state,
                currentModal: action.currentModal,
            };
        case CLOSE_MODAL: 
            return {
                ...state,
                currentModal: null,
            };
        default:
            return state;
    };    
};
