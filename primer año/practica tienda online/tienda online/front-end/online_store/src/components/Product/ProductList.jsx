import { useState } from 'react';
import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { useProducts } from './useProducts';
import ProductForm from './ProductForm';

function ProductList() {
  const {
    products,
    categories,
    error,
    loading,
    deleteProduct,
    createOrUpdateProduct,
  } = useProducts();

  const [showModal, setShowModal] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);

  const openModalForNew = () => {
    setSelectedProduct(null);
    setShowModal(true);
  };

  const openModalForEdit = (product) => {
    setSelectedProduct(product);
    setShowModal(true);
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <Alert variant="danger">{error}</Alert>;

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Products</h2>
        <Button variant="primary" onClick={openModalForNew}>
          Add Product
        </Button>
      </div>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Category</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
              <td>{product.description}</td>
              <td>${product.price}</td>
              <td>{product.stock}</td>
              <td>{product.category?.name}</td>
              <td>
                <Button
                  variant="info"
                  size="sm"
                  className="me-2"
                  onClick={() => openModalForEdit(product)}
                >
                  Edit
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => {
                    if (
                      window.confirm(
                        'Are you sure you want to delete this product?'
                      )
                    ) {
                      deleteProduct(product.id);
                    }
                  }}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <ProductForm
        show={showModal}
        onClose={() => setShowModal(false)}
        onSubmit={createOrUpdateProduct}
        product={selectedProduct}
        categories={categories}
      />
    </div>
  );
}

export default ProductList;
