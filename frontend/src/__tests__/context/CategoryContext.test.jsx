import { renderHook, waitFor } from '@testing-library/react';
import CategoryContext from '../../context/CategoryContext';
import * as categoriesApi from '../../api/categories';

jest.mock('../../api/categories');

describe('CategoryContext', () => {
  it('fetches categories', async () => {
    categoriesApi.getCategories.mockResolvedValue({ data: [{ id: 1, name: 'Books' }] });

    const { result } = renderHook(() => CategoryContext());

    await waitFor(() => {
      expect(result.current.categories[0].name).toBe('Books');
    });
  });
});
