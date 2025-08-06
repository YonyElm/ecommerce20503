import { renderHook, waitFor } from '@testing-library/react';
import { UsersComponentContext } from '../../context/UsersComponentContext';
import * as usersApi from '../../api/users';
import React from 'react';
import { AuthContext } from '../../context/AuthContext';
import { MemoryRouter } from 'react-router-dom';

jest.mock('../../api/users');

const mockUser = { sub: '1', roleName: 'ADMIN' };

describe('UsersComponentContext', () => {
  it('fetches users', async () => {
    usersApi.getUsers.mockResolvedValue({
      data: { users: [{ id: 1 }], roleNames: ['ADMIN'] }
    });

    const wrapper = ({ children }) => (
      <MemoryRouter>
        <AuthContext.Provider value={{ user: mockUser, loading: false }}>
          {children}
        </AuthContext.Provider>
      </MemoryRouter>
    );

    const { result } = renderHook(() => UsersComponentContext(), { wrapper });

    await waitFor(() => {
      expect(result.current.users.length).toBeGreaterThan(0);
    });

    expect(result.current.users[0].id).toBe(1);
    expect(result.current.users[0].roleName).toBe('ADMIN');
  });
});
