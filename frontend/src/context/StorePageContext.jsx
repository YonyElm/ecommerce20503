import { useEffect, useState, useContext } from "react";
import { getStore } from "../api/store";
import { addProduct, updateProduct, deleteProduct } from "../api/products";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";

/**
 * Fetches and manages the user's store products,
 * and all UI-related state/handlers for the Store page.
 */
export function StorePageContext() {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!authContext.loading && !authContext.user) {
      navigate("/");
    }
  }, [authContext.user, authContext.loading, navigate]);

  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!authContext.user) return;
    setLoading(true);
    getStore(authContext.user.sub)
      .then((res) => {
        if (res && res.data) {
          setProducts(res.data.products || []);
          setCategories(res.data.categories || []);
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(err);
        setLoading(false);
      });
  }, [authContext.user]);

  // MODAL state and handlers for Add/Edit Product
  const [isProductModalOpen, setIsProductModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

  const handleOpenAddProduct = () => {
    setEditingProduct(null);
    setIsProductModalOpen(true);
  };

  const handleOpenEditProduct = (product) => {
    setEditingProduct(product);
    setIsProductModalOpen(true);
  };

  const handleCloseProductModal = () => {
    setIsProductModalOpen(false);
    setEditingProduct(null);
  };

  const handleSubmitProduct = async (product) => {
      if (editingProduct && editingProduct.id) {
        const res = await updateProduct(authContext.user.sub, editingProduct.id, product);
        setProducts((items) =>
          items.map((productItem) => {
            if (productItem.id === editingProduct.id) {
              return res.data;
            }
            return productItem;
          })
        );
      } else {
        const res = await addProduct(authContext.user.sub, product);
        setProducts((prev) => [...prev, res.data]);
      }
      handleCloseProductModal();
  };

  const handleDeleteProduct = async (productId) => {
    if (window.confirm("Are you sure you want to delete this product from store?")) {
      await deleteProduct(authContext.user.sub, productId);
      setProducts((prev) =>
        prev.filter((productItem) => productItem.id !== productId)
      );
    }
  };

  return {
    loading,
    error,
    products,
    categories,
    isProductModalOpen,
    editingProduct,
    handleOpenAddProduct,
    handleOpenEditProduct,
    handleCloseProductModal,
    handleSubmitProduct,
    handleDeleteProduct,
    navigate,
  };
}