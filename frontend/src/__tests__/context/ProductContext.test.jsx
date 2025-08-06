import { renderHook, waitFor } from '@testing-library/react';
import ProductContext from '../../context/ProductContext';
import * as productsApi from '../../api/products';

jest.mock('../../api/products');

describe('ProductContext', () => {
  it('fetches all products', async () => {
    productsApi.getProducts.mockResolvedValue({ data: [{ id: 1, name: 'Test' }] });

    const { result } = renderHook(() => ProductContext());

    await waitFor(() => {
      expect(result.current.products[0].name).toBe('Test');
    });
  });

  it('fetches products by category', async () => {
    productsApi.getProductsByCategory.mockResolvedValue({ data: [{ id: 2, name: 'Cat' }] });

    // If your hook expects category IDs as a param, pass it here; else omit.
    const { result } = renderHook(() => ProductContext([5]));

    await waitFor(() => {
      expect(result.current.products[0].id).toBe(2);
    });
  });
});
