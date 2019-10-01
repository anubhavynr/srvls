import config from './config';
import Axios from 'axios';

// import products from '../data/products';

const getAllProducts = () => {
    const url = `${config.api.base_url}/products`;
    let products; 

    Axios.get(url)
        .then(response => {
            products = response;
        })
        .catch(error => {
            console.error(error);
        });

    return products;
};

export default { getAllProducts };
