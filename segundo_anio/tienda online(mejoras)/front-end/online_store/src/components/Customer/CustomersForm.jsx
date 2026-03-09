import { useState, useEffect } from 'react';
import { Modal, Form, Button } from 'react-bootstrap';

const emptyCustomer = {
    firstName: '',
    lastName: '',
    email: '',
    phone: ''
};

const CustomerForm = ({ show, onClose, onSave, onUpdate, customer = null }) => {
    const [formData, setFormData] = useState(emptyCustomer);

    useEffect(() => {
        setFormData(customer || emptyCustomer);
    }, [customer]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            firstName: formData.firstName || "",
            lastName: formData.lastName || "",
            email: formData.email || "",
            phone: formData.phone || ""
        };

        try {
            if (customer && customer.id) {
                await onUpdate(customer.id, payload);
            } else {
                await onSave(payload);
            }
            onClose();
        } catch (err) {
            console.error(err);
            alert(err.message || "Error saving customer");
        }
    };

    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>{customer?.id ? 'Edit Customer' : 'Add Customer'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    {['firstName', 'lastName', 'email', 'phone'].map((field) => (
                        <Form.Group className="mb-3" key={field}>
                            <Form.Label>{field.replace(/([A-Z])/g, ' $1')}</Form.Label>
                            <Form.Control
                                name={field}
                                type={field === 'email' ? 'email' : field === 'phone' ? 'tel' : 'text'}
                                value={formData[field]}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                    ))}
                    <div className="d-flex justify-content-end">
                        <Button variant="secondary" className="me-2" onClick={onClose}>
                            Cancel
                        </Button>
                        <Button type="submit" variant="primary">
                            {customer?.id ? 'Update' : 'Create'}
                        </Button>
                    </div>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default CustomerForm;