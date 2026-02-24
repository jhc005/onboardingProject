
import { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { PlusCircle } from 'react-bootstrap-icons';

const CategoryForm = ({ onAddCategory }) => {
    const [name, setName] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        await onAddCategory({ name });
        setName('');
    };

    return (
        <Form onSubmit={handleSubmit} className="mb-4">
            <Row>
                <Col md={6}>
                    <Form.Control
                        type="text"
                        placeholder="Category name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </Col>
                <Col md={2}>
                    <Button type="submit"><PlusCircle size={20} /></Button>
                </Col>
            </Row>
        </Form>
    );
};

export default CategoryForm;
