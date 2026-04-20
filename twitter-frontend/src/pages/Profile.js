import { useAuth0 } from '@auth0/auth0-react';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState, useCallback } from 'react';
import { useApi } from '../hooks/useApi';
import PostCard from '../components/PostCard';

export default function Profile() {
  const { user, isAuthenticated, isLoading } = useAuth0();
  const { getUserInfo, getAllPosts } = useApi();
  const navigate = useNavigate();
  const [backendUser, setBackendUser] = useState(null);
  const [userPosts, setUserPosts] = useState([]);
  const [loadingPosts, setLoadingPosts] = useState(false);

  useEffect(() => {
    if (!isLoading && !isAuthenticated) navigate('/');
  }, [isLoading, isAuthenticated, navigate]);

  const loadUserData = useCallback(async () => {
    if (!isAuthenticated) return;
    try {
      const [info, allPosts] = await Promise.all([getUserInfo(), getAllPosts()]);
      setBackendUser(info);
      setLoadingPosts(true);
      const postsIds = new Set(info.postsId ?? []);
      const filtered = (Array.isArray(allPosts) ? allPosts : []).filter((p) =>
        postsIds.has(p.postId)
      );
      setUserPosts(filtered);
    } catch {
      setBackendUser(null);
      setUserPosts([]);
    } finally {
      setLoadingPosts(false);
    }
  }, [isAuthenticated, getUserInfo, getAllPosts]);

  useEffect(() => {
    loadUserData();
  }, [loadUserData]);

  if (isLoading) return <p className="text-center text-gray-400 mt-10">Cargando...</p>;
  if (!isAuthenticated) return null;

  return (
    <div className="bg-white border border-gray-200 rounded-xl p-6 flex flex-col items-center gap-4">
      <img src={user.picture} alt={user.name} className="w-20 h-20 rounded-full border-4 border-blue-100" />

      <div className="text-center">
        <h2 className="text-xl font-bold text-gray-900">{user.name}</h2>
        <p className="text-gray-500 text-sm">{user.email}</p>
      </div>

      {backendUser ? (
        <div className="w-full border-t border-gray-100 pt-4 text-sm text-gray-600">
          <p className="font-semibold text-gray-800 mb-2">Datos del servidor</p>
          {backendUser.name && <p>Nombre: {backendUser.name}</p>}
          {backendUser.email && <p>Email: {backendUser.email}</p>}
          <p className="mt-1">Posts: {(backendUser.postsId ?? []).length}</p>
        </div>
      ) : (
        <p className="text-gray-400 text-sm">No se pudo cargar información del servidor.</p>
      )}

      <div className="w-full border-t border-gray-100 pt-4">
        <p className="font-semibold text-gray-800 mb-3 text-sm">Mis publicaciones</p>
        {loadingPosts && <p className="text-gray-400 text-sm text-center">Cargando publicaciones...</p>}
        {!loadingPosts && userPosts.length === 0 && (
          <p className="text-gray-400 text-sm text-center">Aún no has publicado nada.</p>
        )}
        <div className="flex flex-col gap-3">
          {userPosts.map((post) => (
            <PostCard key={post.postId} post={post} canRemove={false} />
          ))}
        </div>
      </div>
    </div>
  );
}
