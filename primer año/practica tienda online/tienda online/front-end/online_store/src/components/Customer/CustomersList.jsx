import { useState } from 'react';
import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { useCustomers } from './useCustomers';
import CustomerForm from './CustomersForm';

function CustomerList() {
    const { customers, loading, error, createCustomer } = useCustomers();
    const [showModal, setShowModal] = useState(false);
    const [editingCustomer, setEditingCustomer] = useState(null);

    const handleAddClick = () => {
        setEditingCustomer(null);
        setShowModal(true);
    };

    const handleEditClick = (customer) => {
        setEditingCustomer(customer);
        setShowModal(true);
    };

    if (loading) return <Spinner animation="border" />;
    if (error) return <Alert variant="danger">{error}</Alert>;

    return (
        <div>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>Customers</h2>
                <Button variant="primary" onClick={handleAddClick}>Add Customer</Button>
            </div>

            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {customers.map((customer) => (
                        <tr key={customer.id}>
                            <td>{customer.id}</td>
                            <td>{customer.firstName}</td>
                            <td>{customer.lastName}</td>
                            <td>{customer.email}</td>
                            <td>{customer.phone}</td>
                            <td>
                                <Button
                                    variant="info"
                                    size="sm"
                                    onClick={() => handleEditClick(customer)}
                                >
                                    Edit
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <CustomerForm
                show={showModal}
                onClose={() => setShowModal(false)}
                onSave={createCustomer}
                customer={editingCustomer}
            />
        </div>
    );
}

export default CustomerList;
