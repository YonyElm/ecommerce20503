import React from 'react';
import ProductCard from './ProductCard';
import ProductContext from '../context/ProductContext';
import Spinner from './Spinner';

function ProductGrid() {
    let { products, loading: isLoading } = ProductContext();

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <Spinner />
            </div>
        );
    }

    if (!products?.length) {
        return <p className="text-center text-gray-500">No products found.</p>;
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {products.map((product) => (
                <ProductCard key={product.id} product={product} />
            ))}
        </div>
    );
}

export default ProductGrid;
