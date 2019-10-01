import { SET_CURRENT_MODAL, CLOSE_MODAL } from '../actions';

const initialState = {
    currentModal: null,
}

export const modal = (state = initialState, action) => {
    switch (action.type) {
        case SET_CURRENT_MODAL: 
            return {
                ...state,
                currentModal: action.currentModal,
            }
        case CLOSE_MODAL: 
            return {
                ...state,
                currentModal: null,
            }
        default:
            return state;
    }    
}
