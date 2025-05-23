import { useState, useEffect } from 'react';
import { productService, categoryService } from '../../services/api';

export const useProducts = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadProducts = async () => {
    try {
      const res = await productService.getAll();
      setProducts(res.data);
    } catch (err) {
      setError('Error loading products');
    } finally {
      setLoading(false);
    }
  };

  const loadCategories = async () => {
    try {
      const res = await categoryService.getAll();
      setCategories(res.data);
    } catch (err) {
      console.error('Error loading categories:', err);
    }
  };

  const deleteProduct = async (id) => {
    try {
      await productService.delete(id);
      await loadProducts();
    } catch (err) {
      setError('Error deleting product');
    }
  };

  const createOrUpdateProduct = async (product) => {
    try {
      if (product.id) {
        await productService.update(product.id, product);
      } else {
        await productService.create(product);
      }
      await loadProducts();
    } catch (err) {
      setError('Error saving product');
      throw err; o
    }
  };

  useEffect(() => {
    loadProducts();
    loadCategories();
  }, []);

  return {
    products,
    categories,
    error,
    loading,
    deleteProduct,
    createOrUpdateProduct,
  };
};
