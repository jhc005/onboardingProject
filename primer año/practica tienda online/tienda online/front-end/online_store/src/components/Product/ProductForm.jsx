import { useState, useEffect } from 'react';
import { Modal, Form, Button } from 'react-bootstrap';

const ProductForm = ({ show, onClose, onSubmit, product, categories }) => {
  const [formProduct, setFormProduct] = useState({
    id: null,
    name: '',
    description: '',
    price: '',
    stock: '',
    categoryId: ''
  });

  useEffect(() => {
    if (product) {
      setFormProduct(product);
    } else {
      setFormProduct({
        id: null, 
        name: '',
        description: '',
        price: '',
        stock: '',
        categoryId: ''
      });
    }
  }, [product, show]);

  const handleChange = (field) => (e) => {
    setFormProduct({ ...formProduct, [field]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
     console.log('Submitting product:', formProduct);
    await onSubmit(formProduct);
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>{formProduct.id ? 'Edit Product' : 'Add Product'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control
              type="text"
              value={formProduct.name}
              onChange={handleChange('name')}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Description</Form.Label>
            <Form.Control
              as="textarea"
              value={formProduct.description}
              onChange={handleChange('description')}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Price</Form.Label>
            <Form.Control
              type="number"
              value={formProduct.price}
              onChange={handleChange('price')}
              required
              min="0"
              step="0.01"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Stock</Form.Label>
            <Form.Control
              type="number"
              value={formProduct.stock}
              onChange={handleChange('stock')}
              required
              min="0"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Category</Form.Label>
            <Form.Select
              value={formProduct.categoryId}
              onChange={handleChange('categoryId')}
              required
            >
              <option value="">Select Category</option>
              {categories.map((cat) => (
                <option key={cat.id} value={cat.id}>
                  {cat.name}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
          <div className="d-flex justify-content-end">
            <Button variant="secondary" className="me-2" onClick={onClose}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              {formProduct.id ? 'Update' : 'Create'}
            </Button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default ProductForm;