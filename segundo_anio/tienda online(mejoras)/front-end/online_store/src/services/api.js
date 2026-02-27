import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

export const categoryService = {
    getAll: () => api.get('/categories'),
    getById: (id) => api.get(`/categories/${id}`),
    create: (category) => api.post('/categories', category)
};

export const productService = {
    getAll: () => api.get('/products'),
    getById: (id) => api.get(`/products/${id}`),
    create: (product) => api.post('/products', product),
    update: (id, product) => api.put(`/products/${id}`, product),
    delete: (id) => api.delete(`/products/${id}`)
};

export const customerService = {
    getAll: () => api.get('/customers'),
    create: (customer) => api.post('/customers', customer),
    update: (id,customer) => api.put(`customers/${id}`, customer)
};

export const orderService = {
    getAll: () => api.get('/orders'),
    create: (order) => api.post('/orders', order)
};