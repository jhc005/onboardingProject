import { useState, useEffect } from 'react';
import { customerService } from '../../services/api';

export const useCustomers = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadCustomers = async () => {
        setLoading(true);
        try {
            const response = await customerService.getAll();
            setCustomers(response.data);
            setError(null);
        } catch (err) {
            console.error(err);
            setError('Error loading customers');
        } finally {
            setLoading(false);
        }
    };

    const createCustomer = async (customer) => {
        try {
            await customerService.create(customer);
            await loadCustomers();
        } catch (err) {
            setError('Error creating customer');
            throw err;
        }
    };

    const updateCustomer = async (id, customer) => {
        try {
            await customerService.update(id, customer);
            await loadCustomers();
        } catch (err) {
            setError('Error updating customer');
            throw err;
        }
    };

    useEffect(() => {
        loadCustomers();
    }, []);

    return {
        customers,
        loading,
        error,
        createCustomer,
        updateCustomer,
        reload: loadCustomers,
    };
};