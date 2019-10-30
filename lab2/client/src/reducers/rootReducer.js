import { combineReducers } from 'redux';
import { modal } from '../components/modals/reducers';
import { products } from '../components/products/reducers';
import { orders } from '../components/orders/reducers';
import { user } from '../components/user/reducers';

const rootReducer = combineReducers({
    modal,
    user,
    products,
    orders,
});

export default rootReducer;
