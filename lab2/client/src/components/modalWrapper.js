import React from 'react';
import { connect } from 'react-redux';
import {
    SIGN_IN_MODAL,
    SIGN_UP_MODAL,
    ADD_PRODUCT_MODAL,
    DELETE_PRODUCT_MODAL,
    EDIT_PRODUCT_MODAL,
    ADD_ORDER_MODAL,
    EDIT_ORDER_MODAL,
    DELETE_ORDER_MODAL,
} from '../actions';
import SignInModal from './signInModal';
import SignUpModal from './signUpModal';
import AddProductModal from './addProductModal';
import DeleteProductModal from './deleteProductModal';
import EditProductModal from './editProductModal';
import AddOrderModal from './addOrderModal';
import EditOrderModal from './editOrderModal';
import DeleteOrderModal from './deleteOrderModal';

function ModalWrapper(props) {
    switch (props.currentModal) {
        case SIGN_IN_MODAL:
            return <SignInModal />
        case SIGN_UP_MODAL:
            return <SignUpModal />
        case ADD_PRODUCT_MODAL:
            return <AddProductModal />
        case DELETE_PRODUCT_MODAL:
            return <DeleteProductModal />
        case EDIT_PRODUCT_MODAL:
            return <EditProductModal />
        case ADD_ORDER_MODAL:
            return <AddOrderModal />
        case EDIT_ORDER_MODAL:
            return <EditOrderModal />
        case DELETE_ORDER_MODAL:
            return <DeleteOrderModal />
        default:
            return null;
    }
}
 
const mapStateToProps = state => {
    return {
        currentModal: state.modal.currentModal,
    };
}

export default connect(mapStateToProps)(ModalWrapper);
