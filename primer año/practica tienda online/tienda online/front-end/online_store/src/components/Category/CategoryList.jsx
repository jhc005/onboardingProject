
import { Table, Spinner, Alert } from 'react-bootstrap';
import CategoryForm from './CategoryForm';
import { useCategories } from './useCategories';
import { categoryService } from '../../services/api';

function CategoryList() {
    const { categories, loading, error, reload } = useCategories();

    const handleAddCategory = async (category) => {
        try {
            await categoryService.create(category);
            reload();
        } catch (err) {
            console.error('Error creating category:', err);
        }
    };

    return (
        <div>
            <h2>Categories</h2>

            <CategoryForm onAddCategory={handleAddCategory} />

            {error && <Alert variant="danger">{error}</Alert>}
            {loading ? (
                <Spinner animation="border" />
            ) : (
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {categories.map((category) => (
                            <tr key={category.id}>
                                <td>{category.id}</td>
                                <td>{category.name}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </div>
    );
}

export default CategoryList;
