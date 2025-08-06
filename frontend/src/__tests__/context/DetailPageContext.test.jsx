import { renderHook, waitFor } from '@testing-library/react';
import {DetailPageContext}  from '../../context/DetailPageContext';
import * as productsApi from '../../api/products';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import * as ordersApi from "../../api/orders";
import {AuthContext} from "../../context/AuthContext";
import React from "react";
import {CartContext, CartProvider} from "../../context/CartContext";

jest.mock('../../api/products');

describe('DetailPageContext', () => {
  it('fetches product details', async () => {
    productsApi.getProductDetailsById.mockResolvedValue({ data: { id: 1, name: 'Test' } });
    const mockUser = { sub: '1' };

    window.history.pushState({}, '', '/details/1');

    const wrapper = ({ children }) => (
      <MemoryRouter initialEntries={['/details/1']}>
          <AuthContext.Provider value={{ user: mockUser, loading: false }}>
            <CartContext.Provider value={{ cartItems: [], submitPlaceOrderForm: jest.fn() }}>
              <Routes>
                <Route path="/details/:productId" element={<>{children}</>} />
              </Routes>
            </CartContext.Provider>
          </AuthContext.Provider>
      </MemoryRouter>
    );

    const { result } = renderHook(() => DetailPageContext(), { wrapper });

    await waitFor(() => {
      expect(result.current.product.id).toBe(1);
    });
  });
});
