import { useEffect, useState } from 'react';
import {getCategories} from "../api/categories";

const CategoryContext = () => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        getCategories()
            .then(res => {
                if (res && res.data != null) {
                    setCategories(res.data);
                    setLoading(false);
                }
            })
            .catch((err) => {
                console.error('Error loading categories', err);
                setLoading(false);
            });
    }, []);

    return { categories, loading };
};

export default CategoryContext;
