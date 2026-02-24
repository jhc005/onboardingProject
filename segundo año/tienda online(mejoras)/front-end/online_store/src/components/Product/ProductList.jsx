import { useState } from 'react';
import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { PencilSquare, Trash, PlusCircle } from 'react-bootstrap-icons';
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
        <Button variant="primary" onClick={openModalForNew}>          <PlusCircle size={20} />
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
              <td>{product.price}</td>
              <td>{product.stock}</td>
              <td>{categories.find(c => c.id === product.categoryId)?.name}</td>
              <td>
                <Button
                  variant="info"
                  size="sm"
                  className="editButton"
                  onClick={() => openModalForEdit(product)}
                >                  <PencilSquare size={16} />
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  className="deleteButton"
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
                  <Trash size={16} />
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
