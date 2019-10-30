import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import {
    addProductModal,
    deleteProductModal,
    editProductModal,
} from '../../modals/actions';
import {
    fetchProducts,
    fetchProductCategories,
} from '../actions';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faEdit,
    faPlus,
    faTrash,
} from '@fortawesome/free-solid-svg-icons';

function Products(props) {
    const { products,
        addProductModal,
        deleteProductModal,
        editProductModal,
        fetchProducts,
        fetchProductCategories,
    } = props;

    useEffect(() => {
            fetchProducts();
    }, [fetchProducts]);

    useEffect(() => {
        fetchProductCategories();
    }, [fetchProductCategories]);

    return (
        <Container>
            <h2>Products</h2>
            <Container>
                <Row className='mb-2'>
                    <Col>
                        <Button onClick={addProductModal} variant='success' className="float-right"> Add Product <FontAwesomeIcon icon={faPlus} /></Button>
                    </Col>
                </Row>
                <Row>
                    <Table>
                        <thead>
                            <tr>
                                <th>SKU</th>
                                <th>Product Name</th>
                                <th>Price</th>
                                <th>Category</th>
                                <th className="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                products ?
                                    products.map((product) => {
                                        const { id,
                                            sku,
                                            name,
                                            price,
                                            category,
                                        } = product;
                                        return (
                                            <tr key={id}>
                                                <td>{sku}</td>
                                                <td>{name}</td>
                                                <td>${price}</td>
                                                <td>{category.name}</td>
                                                <td className="text-center"><Button variant='secondary' onClick={() => editProductModal(product)}> Edit <FontAwesomeIcon icon={faEdit} /></Button> <Button onClick={() => deleteProductModal(product)} variant='danger'> Del <FontAwesomeIcon icon={faTrash} /></Button></td>
                                            </tr>
                                        );
                                    }
                                    )
                                    :
                                    null
                            }
                        </tbody>
                    </Table>
                </Row>
            </Container>
        </Container>
    )
}

const mapStateToProps = state => {
    return {
        products: state.products.products,
    };
};

const mapDispatchToProps = {
    addProductModal,
    deleteProductModal,
    editProductModal,
    fetchProducts,
    fetchProductCategories,
};

export default connect(mapStateToProps, mapDispatchToProps)(Products);
