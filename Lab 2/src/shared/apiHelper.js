import config from './config';
import Axios from 'axios';

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
