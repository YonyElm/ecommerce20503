import React from "react";
import { Link } from "react-router-dom";

const NotFound = ({ message }) => (
    <div className="text-gray-500 flex flex-col items-start gap-2 mb-6">
        <span>{message}</span>
        <Link to="/" className="text-cyan-500 hover:underline font-semibold">
            Start Shopping
        </Link>
    </div>
);

export default NotFound;