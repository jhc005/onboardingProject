import { useState } from 'react';
import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { PlusCircle } from 'react-bootstrap-icons';
import { useOrders } from './useOrder';
import OrderForm from './OrderForm';

function OrderList() {
    const { orders, customers, products, loading, error, createOrder } = useOrders();
    const [showModal, setShowModal] = useState(false);

    if (loading) return <Spinner animation="border" />;
    if (error) return <Alert variant="danger">{error}</Alert>;

    return (
        <div>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>Orders</h2>
                <Button variant="primary" onClick={() => setShowModal(true)} ><PlusCircle size={20} />
                </Button>
            </div>

            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer</th>
                        <th>Status</th>
                        <th>Total</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map((order) => (
                        <tr key={order.id}>
                            <td>{order.id}</td>
                            <td>{`${order.customer?.firstName} ${order.customer?.lastName}`}</td>
                            <td>{order.status}</td>
                            <td>${order.total}</td>
                            <td>{new Date(order.orderDate).toLocaleDateString()}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <OrderForm
                show={showModal}
                onClose={() => setShowModal(false)}
                onSubmit={createOrder}
                customers={customers}
                products={products}
            />
        </div>
    );
}

export default OrderList;