import { combineReducers } from 'redux';
import { modal } from './modal';
import { products } from './products';
import { orders } from './orders';
import { user } from './user';

const rootReducer = combineReducers({
    modal,
    user,
    products,
    orders,
});

export default rootReducer;
