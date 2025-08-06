import { renderHook, waitFor } from '@testing-library/react';
import { OrdersContext } from '../../context/OrdersContext';
import * as ordersApi from '../../api/orders';
import React from 'react';
import { AuthContext } from '../../context/AuthContext';
import { MemoryRouter } from 'react-router-dom';

jest.mock('../../api/orders');

const mockUser = { sub: '1' };

describe('OrdersContext', () => {
  it('fetches orders', async () => {
    ordersApi.getOrdersPage.mockResolvedValue({ data: { success: true, data: [{ id: 1 }] } });

    const wrapper = ({ children }) => (
      <MemoryRouter>
        <AuthContext.Provider value={{ user: mockUser, loading: false }}>
          {children}
        </AuthContext.Provider>
      </MemoryRouter>
    );

    const { result } = renderHook(() => OrdersContext({ adminFlag: false }), { wrapper });

    // waitFor will keep retrying until the condition passes or timeout
    await waitFor(() => {
      expect(result.current.orders[0].id).toBe(1);
    });
  });
});
