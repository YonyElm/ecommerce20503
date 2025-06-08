import React from "react";

/**
 * Displays a single category. Supports selection highlighting and click handling.
 */
function CategoryCard({ category, selected, onClick }) {
    return (
        <button
            type="button"
            onClick={() => onClick(category.id)}
            className={`flex items-center w-full p-4 rounded shadow border transition 
                ${selected ? "border-cyan-400 bg-cyan-50 text-cyan-700 font-bold" : "bg-white border-gray-200 text-gray-700 hover:bg-cyan-50 hover:border-cyan-200"}`}
            aria-pressed={selected}
        >
            <span
                className={`w-5 h-5 flex items-center justify-center mr-3 rounded border 
                    ${selected ? "bg-cyan-400 border-cyan-500" : "bg-white border-gray-400"}`}
            >
                {selected && (
                    <svg className="w-3 h-3 text-white" fill="none" stroke="currentColor" viewBox="0 0 20 20">
                        <path d="M5 11l4 4L15 7" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                    </svg>
                )}
            </span>
            <span className="truncate">{category.name}</span>
        </button>
    );
}

export default CategoryCard;
