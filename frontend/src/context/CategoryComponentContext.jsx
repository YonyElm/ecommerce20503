import {useContext, useEffect, useState} from 'react';
import {getCategories, createCategory, updateCategory, deleteCategory} from '../api/categories';
import {AuthContext} from "./AuthContext";

export function CategoryComponentContext() {
    const authContext = useContext(AuthContext);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // For UI (editing/adding)
    const [newCategoryName, setNewCategoryName] = useState('');
    const [editingId, setEditingId] = useState(null);
    const [editedCategoryName, setEditedCategoryName] = useState('');

    const fetchCategories = async () => {
        setLoading(true);
        try {
            const response = await getCategories();
            setCategories(response.data);
            setError(null);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleChangeNewCategoryName = (e) => {
        setNewCategoryName(e.target.value);
    };

    const handleChangeEditedCategoryName = (e) => {
        setEditedCategoryName(e.target.value);
    };

    const handleAddCategory = async (e) => {
        if (e) e.preventDefault();
        if (!newCategoryName.trim()) return;
        try {
            let dataObj = {categoryName: newCategoryName}
            await createCategory(authContext.user.sub, dataObj);
            setNewCategoryName('');
            await fetchCategories();
        } catch (err) {
            setError(err);
        }
    };

    // Start editing
    const handleEditCategory = (category) => {
        setEditingId(category.id);
        setEditedCategoryName(category.name);
    };

    // Save edit
    const handleUpdateCategory = async (e) => {
        if (e) e.preventDefault();
        if (!editedCategoryName.trim()) return;
        try {
            let dataObj = {categoryName: editedCategoryName}
            await updateCategory(authContext.user.sub, editingId, dataObj);
            setEditingId(null);
            setEditedCategoryName('');
            await fetchCategories();
        } catch (err) {
            setError(err);
        }
    };

    // Cancel edit
    const handleCancelEdit = () => {
        setEditingId(null);
        setEditedCategoryName('');
    };

    const handleDeleteCategory = async (categoryId) => {
        try {
            await deleteCategory(authContext.user.sub, categoryId);
            await fetchCategories();
        } catch (err) {
            setError(err);
        }
    };

    return {
        categories,
        loading,
        error,
        // Add
        newCategoryName,
        handleChangeNewCategoryName,
        handleAddCategory,
        // Edit
        editingId,
        editedCategoryName,
        handleEditCategory,
        handleChangeEditedCategoryName,
        handleUpdateCategory,
        handleCancelEdit,
        handleDeleteCategory,
    };
}