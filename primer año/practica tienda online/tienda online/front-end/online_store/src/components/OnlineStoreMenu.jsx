import { Card, Row, Col, Container } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import '../OnlineStoreMenu.css';

function OnlineStoreMenu() {
    const navigate = useNavigate();

    const menuItems = [
        {
            title: 'Categories',
            icon: '📑',
            description: 'Manage product categories',
            path: '/categories',
        },
        {
            title: 'Products',
            icon: '📦',
            description: 'View and manage products',
            path: '/products',
        },
        {
            title: 'Customers',
            icon: '👥',
            description: 'Customer management',
            path: '/customers',
        },
        {
            title: 'Orders',
            icon: '🛍️',
            description: 'View and manage orders',
            path: '/orders',
        },
        {
            title: 'Statistics',
            icon: '📊',
            description: 'View sales statistics',
            path: '/statistics',
        }
    ];

    return (
        <Container className="mt-5">
            <h1 className="text-center mb-4">Online Store Management</h1>
            <Row className="justify-content-center">
                {menuItems.map((item, index) => (
                    <Col xs={12} md={6} lg={3} key={index} className="mb-4">
                        <Card 
                            className="menu-card" 
                            onClick={() => navigate(item.path)}
                            style={{ borderTop: `4px solid ${item.color}` }}
                        >
                            <Card.Body className="text-center">
                                <div className="menu-icon mb-3">
                                    {item.icon}
                                </div>
                                <Card.Title>{item.title}</Card.Title>
                                <Card.Text className="text-muted">
                                    {item.description}
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}

export default OnlineStoreMenu;
