import React, { useState } from 'react';
import ProductGrid from '../components/ProductGrid';
import CategoryPanel from '../components/CategoryPanel';

const Home = () => {
    const [selectedCategories, setSelectedCategories] = useState([]);

    return (
        <div className="p-4">
            <div className="flex flex-col md:flex-row gap-8">
                <CategoryPanel
                    selectedCategoryIds={selectedCategories}
                    onSelectionChange={setSelectedCategories}
                />
                <div className="flex-1">
                    <ProductGrid selectedCategories={selectedCategories} />
                </div>
            </div>
        </div>
    );
};

export default Home;
