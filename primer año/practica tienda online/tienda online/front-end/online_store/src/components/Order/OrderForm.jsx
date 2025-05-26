import { useState, useEffect } from 'react';
import { Modal, Form, Button } from 'react-bootstrap';

const OrderForm = ({ show, onClose, onSubmit, customers, products }) => {
    const [order, setOrder] = useState({
        customerId: '',
        products: [],
        status: 'PENDING',
        total: 0
    });

    useEffect(() => {
        setOrder({
            customerId: '',
            products: [],
            status: 'PENDING',
            total: 0
        });
    }, [show]);

    const handleProductToggle = (product, checked) => {
        const updatedProducts = checked
            ? [...order.products, product]
            : order.products.filter(p => p.id !== product.id);

        const total = updatedProducts.reduce((sum, p) => sum + p.price, 0);

        setOrder({ ...order, products: updatedProducts, total });
    };

    const formattedDate = (date) => {
        return date.toISOString().split('.')[0];
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const orderToSend = {
            customer: { id: Number(order.customerId) },
            status: order.status.toUpperCase(),
            total: order.total,
            orderDate: formattedDate(new Date()),
        };

        console.log('Submitting order:', orderToSend);
        await onSubmit(orderToSend);
        onClose();
    };

    return (
        <Modal show={show} onHide={onClose} size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Create New Order</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Customer</Form.Label>
                        <Form.Select
                            value={order.customerId}
                            onChange={(e) =>
                                setOrder({ ...order, customerId: e.target.value })
                            }
                        >
                            <option value="">Select Customer</option>
                            {customers.map((c) => (
                                <option key={c.id} value={c.id}>
                                    {`${c.firstName} ${c.lastName}`}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Products</Form.Label>
                        <div className="border p-3 rounded">
                            {products.map((product) => (
                                <Form.Check
                                    key={product.id}
                                    type="checkbox"
                                    label={`${product.name} - $${product.price}`}
                                    onChange={(e) =>
                                        handleProductToggle(product, e.target.checked)
                                    }
                                />
                            ))}
                        </div>
                    </Form.Group>

                    <div className="d-flex justify-content-between align-items-center">
                        <h5>Total: ${order.total}</h5>
                        <div>
                            <Button variant="secondary" className="me-2" onClick={onClose}>
                                Cancel
                            </Button>
                            <Button
                                variant="primary"
                                type="submit"
                                disabled={!order.customerId || order.products.length === 0}
                            >
                                Create Order
                            </Button>
                        </div>
                    </div>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default OrderForm;
