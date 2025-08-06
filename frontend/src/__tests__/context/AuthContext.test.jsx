import React from 'react';
import { render, act } from '@testing-library/react';
import { AuthProvider, AuthContext } from '../../context/AuthContext';

const mockJwtDecode = jest.fn();
jest.mock('jwt-decode', () => ({ jwtDecode: () => mockJwtDecode() }));

describe('AuthContext', () => {
  beforeEach(() => {
    localStorage.clear();
    mockJwtDecode.mockReset();
  });

  it('should provide null user if no token', () => {
    let contextValue;
    render(
      <AuthProvider>
        <AuthContext.Consumer>
          {value => { contextValue = value; return null; }}
        </AuthContext.Consumer>
      </AuthProvider>
    );
    expect(contextValue.user).toBeNull();
  });

  it('should set user on login', () => {
    let contextValue;
    mockJwtDecode.mockReturnValue({ sub: '123' });
    render(
      <AuthProvider>
        <AuthContext.Consumer>
          {value => { contextValue = value; return null; }}
        </AuthContext.Consumer>
      </AuthProvider>
    );
    act(() => {
      contextValue.login('token');
    });
    expect(contextValue.user).toEqual({ sub: '123' });
  });

  it('should clear user on logout', () => {
    let contextValue;
    mockJwtDecode.mockReturnValue({ sub: '123' });
    render(
      <AuthProvider>
        <AuthContext.Consumer>
          {value => { contextValue = value; return null; }}
        </AuthContext.Consumer>
      </AuthProvider>
    );
    act(() => {
      contextValue.login('token');
      contextValue.logout();
    });
    expect(contextValue.user).toBeNull();
  });
});
