// src/components/Footer.jsx
import React from "react";

const Footer = () => {
    return (
        <footer className="bg-[#131921] text-gray-300 mt-16 pt-10 pb-6">
            <div className="container mx-auto px-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-8">
                <div>
                    <h3 className="font-bold text-lg mb-4 text-white">Get to Know Us</h3>
                    <ul className="space-y-2 text-sm">
                        <li><a href="#" className="hover:underline">About Us</a></li>
                        <li><a href="#" className="hover:underline">Careers</a></li>
                        <li><a href="#" className="hover:underline">Press Releases</a></li>
                        <li><a href="#" className="hover:underline">Blog</a></li>
                    </ul>
                </div>

                <div>
                    <h3 className="font-bold text-lg mb-4 text-white">Connect with Us</h3>
                    <ul className="space-y-2 text-sm">
                        <li><a href="#" className="hover:underline">Github</a></li>
                        <li><a href="#" className="hover:underline">LinkedIn</a></li>
                    </ul>
                </div>

                <div>
                    <h3 className="font-bold text-lg mb-4 text-white">Make Money with Us</h3>
                    <ul className="space-y-2 text-sm">
                        <li><a href="#" className="hover:underline">Sell on Our Platform</a></li>
                        <li><a href="#" className="hover:underline">Affiliate Program</a></li>
                        <li><a href="#" className="hover:underline">Advertise Your Products</a></li>
                    </ul>
                </div>

                <div>
                    <h3 className="font-bold text-lg mb-4 text-white">Let Us Help You</h3>
                    <ul className="space-y-2 text-sm">
                        <li><a href="#" className="hover:underline">Your Account</a></li>
                        <li><a href="#" className="hover:underline">Returns Centre</a></li>
                        <li><a href="#" className="hover:underline">Help</a></li>
                        <li><a href="#" className="hover:underline">Contact Us</a></li>
                    </ul>
                </div>
            </div>

            <div className="mt-10 border-t border-gray-700 pt-4 text-center text-sm text-gray-400">
                &copy; 2025 Ecommerce, Inc. All rights reserved.
            </div>
        </footer>
    );
};

export default Footer;
