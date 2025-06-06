import Spinner from "./Spinner";
import CategoryContext from "../context/CategoryContext";
import CategoryCard from "./CategoryCard";

/**
 * Sidebar panel using CategoryContext for category data.
 * Each category is shown with a CategoryCard.
 */
function CategoryPanel({ selectedCategoryIds, onSelectionChange }) {
    const { categories, loading } = CategoryContext();

    function handleCategoryClick(categoryId) {
        const next = selectedCategoryIds.includes(categoryId)
            ? selectedCategoryIds.filter(id => id !== categoryId)
            : [...selectedCategoryIds, categoryId];
        onSelectionChange(next);
    }

    return (
        <aside className="w-full md:w-1/6 sidebar bg-white p-6 rounded-lg shadow border border-gray-200 h-fit">
            <h2 className="text-xl font-bold mb-4 text-gray-800">Shop by Category</h2>
            {loading ? (
                <div className="flex justify-center items-center h-24">
                    <Spinner />
                </div>
            ) : (
                <nav>
                    <ul>
                        {categories.map(category => (
                            <li className="mb-2" key={category.id}>
                                <CategoryCard
                                    category={category}
                                    selected={selectedCategoryIds.includes(category.id)}
                                    onClick={() => handleCategoryClick(category.id)}
                                    customClass={
                                        selectedCategoryIds.includes(category.id)
                                            ? "active text-gray-700 font-bold border-l-4 border-gray-700 bg-transparent"
                                            : "text-gray-700 hover:bg-gray-100 hover:text-cyan-700"
                                    }
                                />
                            </li>
                        ))}
                    </ul>
                </nav>
            )}
        </aside>
    );
}

export default CategoryPanel;