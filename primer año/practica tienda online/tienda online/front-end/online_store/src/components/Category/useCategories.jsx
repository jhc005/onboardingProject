
import { useState, useEffect } from 'react';
import { categoryService } from '../../services/api';

export const useCategories = () => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const loadCategories = async () => {
        setLoading(true);
        try {
            const response = await categoryService.getAll();
            setCategories(response.data);
            setError(null);
        } catch (err) {
            console.error('Error loading categories:', err);
            setError('Failed to load categories.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadCategories();
    }, []);

    return { categories, loading, error, reload: loadCategories };
};
