// src/components/ProductCard.jsx
import { Link } from "react-router-dom";
import { MdImageNotSupported } from "react-icons/md";

function ProductCard({ product }) {
    return (
        <div className="border rounded-lg shadow p-4 bg-white hover:shadow-lg transition">
            {product.imageUrl ? (
                <img src={product.imageUrl} alt={product.name} />
            ) : (
                <div className="flex items-center justify-center w-full h-48 bg-gray-100 text-gray-400">
                    <MdImageNotSupported size={48} />
                </div>
            )}

            <h3 className="text-lg font-semibold mb-1">{product.name}</h3>
            <p className="text-gray-600 mb-2">${product.price.toFixed(2)}</p>
            <Link to={`/products/${product.id}`}  className="text-blue-600 hover:underline">
                View Details
            </Link>
        </div>
    );
}

export default ProductCard;
