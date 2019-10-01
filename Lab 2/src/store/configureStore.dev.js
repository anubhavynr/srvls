import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import rootReducer from '../reducers';

const configureStore = () => {
    const store = createStore(
        rootReducer,
        applyMiddleware(thunk)
    )

    // if (module.hot) {
    //     module.hot.accept('../reducers', () => {
    //         store.replaceReducer(rootReducer);
    //     })
    // }

    return store;
}

export default configureStore;
