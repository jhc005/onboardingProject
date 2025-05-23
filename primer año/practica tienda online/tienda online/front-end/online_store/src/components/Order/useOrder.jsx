import { useState, useEffect } from 'react';
import { orderService, customerService, productService } from '../../services/api';

export const useOrders = () => {
    const [orders, setOrders] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    const loadOrders = async () => {
        try {
            const res = await orderService.getAll();
            setOrders(res.data);
        } catch (err) {
            setError('Error loading orders');
        } finally {
            setLoading(false);
        }
    };

    const loadCustomers = async () => {
        try {
            const res = await customerService.getAll();
            setCustomers(res.data);
        } catch (err) {
            console.error('Error loading customers:', err);
        }
    };

    const loadProducts = async () => {
        try {
            const res = await productService.getAll();
            setProducts(res.data);
        } catch (err) {
            console.error('Error loading products:', err);
        }
    };

    const createOrder = async (order) => {
        await orderService.create(order);
        await loadOrders();
    };

    useEffect(() => {
        loadOrders();
        loadCustomers();
        loadProducts();
    }, []);

    return {
        orders,
        customers,
        products,
        loading,
        error,
        createOrder,
    };
};