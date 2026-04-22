import { useCallback } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

export const API_BASE = process.env.REACT_APP_API_BASE_URL || 'http://localhost:4040';
export const FEED_API_BASE = process.env.REACT_APP_FEED_API_URL || process.env.REACT_APP_API_BASE_URL || 'http://localhost:4040';

export function useApi() {
  const { getAccessTokenSilently } = useAuth0();

  const authFetch = useCallback(
    async (url, options = {}) => {
      const token = await getAccessTokenSilently({
        authorizationParams: { audience: 'https://twitter-api' },
      });
      return fetch(url, {
        ...options,
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
          ...options.headers,
        },
      });
    },
    [getAccessTokenSilently]
  );

  const getStream = useCallback(
    () => fetch(`${FEED_API_BASE}/api/stream`).then((r) => r.json()),
    []
  );

  const getAllPosts = useCallback(
    () => fetch(`${API_BASE}/api/posts`).then((r) => r.json()),
    []
  );

  const createPost = useCallback(
    async (message, userId, userEmail) => {
      const res = await authFetch(`${API_BASE}/api/posts/create`, {
        method: 'POST',
        body: JSON.stringify({ message, userId, userEmail }),
      });
      if (!res.ok) throw new Error('Error al crear publicación');
      return res.json();
    },
    [authFetch]
  );

  const removeFromStream = useCallback(
    async (postId) => {
      const res = await authFetch(`${API_BASE}/api/posts/${postId}`, {
        method: 'DELETE',
      });
      if (!res.ok) throw new Error('Error al eliminar del stream');
    },
    [authFetch]
  );

  const getUserInfo = useCallback(
    async () => {
      const res = await authFetch(`${API_BASE}/api/users/me`);
      if (!res.ok) throw new Error('Usuario no encontrado');
      return res.json();
    },
    [authFetch]
  );

  const registerMe = useCallback(
    async (name, email) => {
      const res = await authFetch(`${API_BASE}/api/users/me`, {
        method: 'POST',
        body: JSON.stringify({ name, email }),
      });
      if (!res.ok) throw new Error('Error al registrar usuario');
      return res.json();
    },
    [authFetch]
  );

  return { getStream, getAllPosts, createPost, removeFromStream, getUserInfo, registerMe };
}
