import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Container, Nav, Navbar } from 'react-bootstrap';
import CategoryList from './components/Category/CategoryList';
import ProductList from './components/Product/ProductList';
import CustomerList from './components/Customer/CustomersList';
import OrderList from './components/Order/OrderList';
import OnlineStoreMenu from './components/OnlineStoreMenu';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
    return (
        <Router>
            <div>
                <Navbar bg="dark" variant="dark" expand="lg">
                    <Container>
                        <Navbar.Brand href="/">Online Store</Navbar.Brand>
                        <Navbar.Toggle />
                        <Navbar.Collapse>
                            <Nav className="me-auto">
                                <Nav.Link href="/categories">Categories</Nav.Link>
                                <Nav.Link href="/products">Products</Nav.Link>
                                <Nav.Link href="/customers">Customers</Nav.Link>
                                <Nav.Link href="/orders">Orders</Nav.Link>
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
                    </Routes>
                </Container>
            </div>
        </Router>
    );
}

export default App;
