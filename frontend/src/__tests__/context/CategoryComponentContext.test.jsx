import { renderHook, act, waitFor } from '@testing-library/react';
import { CategoryComponentContext } from '../../context/CategoryComponentContext';
import * as categoriesApi from '../../api/categories';
import React from 'react';
import { AuthContext } from '../../context/AuthContext';

jest.mock('../../api/categories');

const mockUser = { sub: '1' };

describe('CategoryComponentContext', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('fetches categories on mount', async () => {
    categoriesApi.getCategories.mockResolvedValue({ data: [{ id: 1, name: 'Books' }] });
    const wrapper = ({ children }) => (
      <AuthContext.Provider value={{ user: mockUser }}>
        {children}
      </AuthContext.Provider>
    );

    const { result } = renderHook(() => CategoryComponentContext(), { wrapper });

    await waitFor(() => {
      expect(result.current.categories[0].name).toBe('Books');
    });
  });
});
