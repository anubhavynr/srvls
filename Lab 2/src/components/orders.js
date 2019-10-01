import React from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';

function Dashboard() {
    return (
        <Container>
            <Row>
                <h2>Tenant Name</h2>
            </Row>
            <Row>
                <h3>Orders...</h3>
            </Row>
        </Container>
    )
}

export default Dashboard;
