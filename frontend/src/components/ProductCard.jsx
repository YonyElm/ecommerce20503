// src/components/ProductCard.jsx
import { Link } from "react-router-dom";

function ProductCard({ product }) {
    return (
        <div className="border rounded-lg shadow p-4 bg-white hover:shadow-lg transition">
            <img
                src={product.imageUrl || "/placeholder.jpg"}
                alt={product.name}
                className="w-full h-48 object-cover mb-3 rounded"
            />
            <h3 className="text-lg font-semibold mb-1">{product.name}</h3>
            <p className="text-gray-600 mb-2">${product.price.toFixed(2)}</p>
            <Link
                to={`/products/${product.id}`}
                className="text-blue-600 hover:underline"
            >
                View Details
            </Link>
        </div>
    );
}

export default ProductCard;
