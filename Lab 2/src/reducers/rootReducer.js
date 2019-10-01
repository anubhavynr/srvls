import { combineReducers } from 'redux';
import { modal } from './modal';
import { products } from './products';
import { user } from './user';

const rootReducer = combineReducers({
    modal,
    user,
    products,
});

export default rootReducer;
