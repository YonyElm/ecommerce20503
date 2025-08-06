import { renderHook } from '@testing-library/react';
import { UserSettingsPageContext } from '../../context/UserSettingsPageContext';
import * as api from '../../api/userSettings';
import React from 'react';
import { AuthContext } from '../../context/AuthContext';
import { MemoryRouter } from 'react-router-dom';
import { waitFor } from '@testing-library/react';

jest.mock('../../api/userSettings');

const mockUser = { sub: '1', roleName: 'CUSTOMER' };

describe('UserSettingsPageContext', () => {
  it('fetches user settings', async () => {
    api.getUserSettings.mockResolvedValue({
        data: {
          success: true,
          data: {
            user: { id: 1 },
            addresses: [],
            payments: [],
          },
        },
      }
    );

    const wrapper = ({ children }) => (
      <MemoryRouter>
        <AuthContext.Provider value={{ user: mockUser, loading: false, logout: jest.fn(), login: jest.fn() }}>
          {children}
        </AuthContext.Provider>
      </MemoryRouter>
    );

    const { result } = renderHook(() => UserSettingsPageContext(), { wrapper });

    await waitFor(() => {
      expect(result.current.profile?.id).toBe(1);
    });
  });
});
