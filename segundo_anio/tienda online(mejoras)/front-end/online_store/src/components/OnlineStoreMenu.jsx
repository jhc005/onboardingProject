import { Card, Row, Col, Container, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import '../OnlineStoreMenu.css';
import { menuItems } from '../config/menuItems';

function OnlineStoreMenu() {
    const navigate = useNavigate();

    return (
        <div className="dashboard-container">
            <div className="hero-section">
                <Container>
                    <Row className="align-items-center" style={{ minHeight: '60vh' }}>
                        <Col md={6}>
                            <h1 className="hero-title">Online Store Management System</h1>
                            <p className="hero-description">
                                Manage your online store efficiently and professionally.
                                Control inventory, orders, customers and more from a single panel.
                            </p>
                            <div className="hero-stats">
                                <div className="stat-item">
                                    <h3>Complete Management</h3>
                                    <p>Total control of your business</p>
                                </div>
                                <div className="stat-item">
                                    <h3>Real-Time Analysis</h3>
                                    <p>Detailed statistics</p>
                                </div>
                            </div>
                        </Col>
                        <Col md={6} className="text-center">
                            <img
                                src="https://images.unsplash.com/photo-1616941351479-0072624fca9a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                                alt="Dashboard Illustration"
                                className="hero-image"
                            />
                        </Col>
                    </Row>
                </Container>
            </div>

            <Container className="users-section py-5">
                <h2 className="text-center mb-5">Users</h2>
                <Row className="justify-content-center">
                    <Col xs={12} md={4} className="mb-4">
                        <Card className="text-center user-stat-card">
                            <Card.Body>
                                <h3>Total Users</h3>
                                <div className="stat-number">15,847</div>
                                <p>Registered users</p>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col xs={12} md={4} className="mb-4">
                        <Card className="text-center user-stat-card">
                            <Card.Body>
                                <h3>Current Users</h3>
                                <div className="stat-number">2,534</div>
                                <p>Users this month</p>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col xs={12} md={4} className="mb-4">
                        <Card className="text-center user-stat-card">
                            <Card.Body>
                                <h3>Active Users</h3>
                                <div className="stat-number">892</div>
                                <p>Currently online</p>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>

            <Container className="features-section py-5">
                <h2 className="text-center mb-5">Operation Panel</h2>
                <Row className="justify-content-center">
                    {menuItems.map((item, index) => (
                        <Col xs={12} md={6} lg={3} key={index} className="mb-4">
                            <Card
                                className="feature-card"
                                onClick={() => navigate(item.path)}
                            >
                                <Card.Body className="text-center">
                                    <div className="feature-icon mb-3">
                                        {item.icon}
                                    </div>
                                    <Card.Title>{item.title}</Card.Title>
                                    <Card.Text>
                                        {item.description}
                                    </Card.Text>
                                    <Button
                                        variant="outline-primary"
                                        className="mt-3"
                                        onClick={() => navigate(item.path)}
                                    >
                                        Access
                                    </Button>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Container>

            {/* Bottom Section */}
            <div className="bottom-section py-5">
                <Container>
                    <Row className="justify-content-center text-center">
                        <Col md={8}>
                            <h2>¿Need Help?</h2>
                            <p className="mb-4">
                                Our support team is available 24/7 to assist you with any issues or questions you may have.
                            </p>
                            <Button variant="primary" size="lg">
                                Support Contact
                            </Button>
                        </Col>
                    </Row>
                </Container>
            </div>
        </div>
    );
}

export default OnlineStoreMenu;
