import { renderHook, waitFor } from '@testing-library/react';
import CheckoutContext from '../../context/CheckoutContext';
import * as checkoutApi from '../../api/checkout';
import React from 'react';
import { AuthContext } from '../../context/AuthContext';
import { CartContext } from '../../context/CartContext';
import { MemoryRouter } from 'react-router-dom';

jest.mock('../../api/checkout');

const mockUser = { sub: '1' };

const wrapper = ({ children }) => (
  <MemoryRouter>
    <AuthContext.Provider value={{ user: mockUser, loading: false }}>
      <CartContext.Provider value={{ cartItems: [], submitPlaceOrderForm: jest.fn() }}>
        {children}
      </CartContext.Provider>
    </AuthContext.Provider>
  </MemoryRouter>
);

describe('CheckoutContext', () => {
  it('fetches checkout details', async () => {
    checkoutApi.getCheckoutDetailsByUserId.mockResolvedValue({
      data: {
        shippingAddressList: [{ id: 1 }],
        paymentMethodList: [{ id: 2 }],
      },
    });

    const { result } = renderHook(() => CheckoutContext(), { wrapper });

    await waitFor(() => {
      expect(result.current.shippingAddressList[0].id).toBe(1);
      expect(result.current.paymentMethodList[0].id).toBe(2);
    });
  });
});
