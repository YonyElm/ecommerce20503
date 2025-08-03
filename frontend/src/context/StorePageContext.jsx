import { useEffect, useState, useContext } from "react";
import { getStore } from "../api/store";
import { addProduct, updateProduct, deleteProduct } from "../api/products";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";

export function StorePageContext() {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    if (!authContext.user) {
      return;
    }

    const userContext = authContext.user;
    if (!userContext ||
      (userContext.roleName !== "ADMIN" && userContext.roleName !== "SELLER")) {
      setLoading(false);
      navigate("/");
      return;
    }

    if (!userContext.sub) {
      setProducts([]);
      setCategories([]);
      setLoading(false);
      return;
    }

    getStore(authContext.user.sub)
      .then((res) => {
        if (res && res.data) {
          setProducts(res.data.products || []);
          setCategories(res.data.categories || []);
        }
      })
      .catch((err) => {
        setProducts([]);
        setCategories([]);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [authContext.user, authContext.loading, navigate]);

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

  const handleSubmitProduct = async (product, file) => {
    const userId = authContext.user.sub;
    let res;

    // If a file exists, use FormData; otherwise, use the product object directly.
    let requestData;
    if (file) {
      requestData = new FormData();
      Object.entries(product).forEach(([key, value]) => {
        requestData.append(key, value);
      });
      requestData.append("image", file);
    } else {
      requestData = product;
    }

    // Determine which API function to call and execute it.
    const isUpdating = editingProduct && editingProduct.id;
    if (isUpdating) {
      res = await updateProduct(userId, editingProduct.id, requestData, !!file);
    } else {
      res = await addProduct(userId, requestData, !!file);
    }

    // Update the product list based on the response.
    if (res.data) {
      setProducts((items) => {
        // Find and replace the updated product, or add the new one.
        const updatedList = items.map((productItem) =>
          productItem.id === res.data.id ? res.data : productItem
        );
        // If the product wasn't in the list (i.e., a new product), add it.
        if (!isUpdating) {
          return [...updatedList, res.data];
        }
        return updatedList;
      });
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