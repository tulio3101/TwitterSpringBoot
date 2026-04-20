import { useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { useApi } from './useApi';

export function useRegisterUser() {
  const { isAuthenticated, user } = useAuth0();
  const { registerMe } = useApi();

  useEffect(() => {
    if (!isAuthenticated || !user) return;
    registerMe(user.name, user.email).catch(() => {});
  }, [isAuthenticated, user]);
}
