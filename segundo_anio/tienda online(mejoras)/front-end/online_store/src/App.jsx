import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Container, Nav, Navbar } from 'react-bootstrap';
import CategoryList from './components/Category/CategoryList';
import ProductList from './components/Product/ProductList';
import CustomerList from './components/Customer/CustomersList';
import OrderList from './components/Order/OrderList';
import StatisticsView from './components/Statistics/StatisticsView';
import OnlineStoreMenu from './components/OnlineStoreMenu';
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles/global.css';

function App() {
    return (
        <Router>
            <div>
                <Navbar expand="lg" className="navbar">
                    <Container>
                        <Navbar.Brand href="/" style={{ color: 'var(--orange-primary)' }}>Online Store</Navbar.Brand>
                        <Navbar.Toggle aria-controls="basic-navbar-nav" />
                        <Navbar.Collapse id="basic-navbar-nav">
                            <Nav className="me-auto">
                                <Nav.Link href="/categories">Categories</Nav.Link>
                                <Nav.Link href="/products">Products</Nav.Link>
                                <Nav.Link href="/customers">Customers</Nav.Link>
                                <Nav.Link href="/orders">Orders</Nav.Link>
                                <Nav.Link href="/statistics">Statistics</Nav.Link>
                            </Nav>
                        </Navbar.Collapse>
                    </Container>
                </Navbar>

                <Container className="mt-4">
                    <Routes>
                        <Route path="/" element={<OnlineStoreMenu />} />
                        <Route path="/categories" element={<CategoryList />} />
                        <Route path="/products" element={<ProductList />} />
                        <Route path="/customers" element={<CustomerList />} />
                        <Route path="/orders" element={<OrderList />} />
                        <Route path="/statistics" element={<StatisticsView />} />
                    </Routes>
                </Container>
            </div>
        </Router>
    );
}

export default App;
