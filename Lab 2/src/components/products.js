import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { addProductModal, fetchProducts } from '../actions';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';
import Button from 'react-bootstrap/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEdit, faPlus, faTrash } from '@fortawesome/free-solid-svg-icons'

function Products(props) {
    const { products,
        addProductModal,
        fetchProducts,
    } = props;

    useEffect(() => {
            fetchProducts();
    }, [fetchProducts]);

    return (
        <Container>
            <h2>Products</h2>
            <Container>
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
                                    return (
                                        <tr key={product.id}>
                                            <td>{product.sku}</td>
                                            <td>{product.name}</td>
                                            <td>${product.price}</td>
                                            <td>{product.category.name}</td>
                                            <td className="text-center"><Button variant='secondary' href="/products"> Edit <FontAwesomeIcon icon={faEdit} /></Button> <Button href="/products" variant='danger'> Del <FontAwesomeIcon icon={faTrash} /></Button></td>
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
                <Row>
                    <Col>
                        <Button onClick={addProductModal} variant='success' className="float-right"> Add Product <FontAwesomeIcon icon={faPlus} /></Button>
                    </Col>
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

export default connect(mapStateToProps, { addProductModal, fetchProducts })(Products);
