import { renderHook, waitFor } from '@testing-library/react';
import { StorePageContext } from '../../context/StorePageContext';
import { AuthContext } from '../../context/AuthContext';
import * as storeApi from '../../api/store';
import * as productsApi from '../../api/products';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';

jest.mock('../../api/store');
jest.mock('../../api/products');

const mockUser = { sub: '1', roleName: 'ADMIN' };

describe('StorePageContext', () => {
  it('fetches store data correctly', async () => {
    storeApi.getStore.mockResolvedValue({
      data: {
        products: [{ id: 1, name: "Product A" }],
        categories: [{ id: 2, name: "Category B" }]
      }
    });

    const wrapper = ({ children }) => (
      <MemoryRouter>
        <AuthContext.Provider value={{ user: mockUser }}>
          {children}
        </AuthContext.Provider>
      </MemoryRouter>
    );

    const { result } = renderHook(() => StorePageContext(), { wrapper });

    await waitFor(() => {
      expect(result.current.products.length).toBe(1);
      expect(result.current.categories.length).toBe(1);
      expect(result.current.products[0].name).toBe("Product A");
      expect(result.current.categories[0].name).toBe("Category B");
      expect(result.current.loading).toBe(false);
    });
  });
});
