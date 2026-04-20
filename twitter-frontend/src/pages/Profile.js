import { useAuth0 } from '@auth0/auth0-react';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useApi } from '../hooks/useApi';

export default function Profile() {
  const { user, isAuthenticated, isLoading } = useAuth0();
  const { getUserInfo } = useApi();
  const navigate = useNavigate();
  const [backendUser, setBackendUser] = useState(null);

  useEffect(() => {
    if (!isLoading && !isAuthenticated) navigate('/');
  }, [isLoading, isAuthenticated, navigate]);

  useEffect(() => {
    if (!isAuthenticated) return;
    getUserInfo()
      .then(setBackendUser)
      .catch(() => setBackendUser(null));
  }, [isAuthenticated, getUserInfo]);

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
        </div>
      ) : (
        <p className="text-gray-400 text-sm">No se pudo cargar información del servidor.</p>
      )}
    </div>
  );
}
