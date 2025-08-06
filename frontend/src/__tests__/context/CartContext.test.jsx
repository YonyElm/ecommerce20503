import React from 'react';
import { render, act } from '@testing-library/react';
import { CartProvider, CartContext } from '../../context/CartContext';
import { AuthContext } from '../../context/AuthContext';

const mockGetCart = jest.fn();
jest.mock('../../api/cart', () => ({ getCart: (...args) => mockGetCart(...args) }));

const mockUser = { sub: '1' };

describe('CartContext', () => {
  beforeEach(() => {
    localStorage.clear();
    mockGetCart.mockReset();
  });

  it('should provide cartItems and addItem', async () => {
    let contextValue;
    mockGetCart.mockResolvedValue({ data: [{ productId: 1, quantity: 2 }] });
    await act(async () => {
      render(
        <AuthContext.Provider value={{ user: mockUser }}>
          <CartProvider>
            <CartContext.Consumer>
              {value => { contextValue = value; return null; }}
            </CartContext.Consumer>
          </CartProvider>
        </AuthContext.Provider>
      );
    });
    expect(Array.isArray(contextValue.cartItems)).toBe(true);
    expect(contextValue.cartItems[0].productId).toBe(1);
  });
});
