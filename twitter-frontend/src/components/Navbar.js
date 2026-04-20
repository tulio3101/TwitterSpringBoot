import { Link } from 'react-router-dom';
import { useAuth0 } from '@auth0/auth0-react';

export default function Navbar() {
  const { isAuthenticated, loginWithRedirect, logout, user } = useAuth0();

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-10">
      <div className="max-w-2xl mx-auto px-4 h-14 flex items-center justify-between">
        <Link to="/" className="text-xl font-bold text-blue-500">
          Twitter
        </Link>

        <div className="flex items-center gap-3">
          {isAuthenticated ? (
            <>
              <Link to="/profile" className="flex items-center gap-2 text-sm text-gray-700 hover:text-blue-500">
                <img src={user.picture} alt={user.name} className="w-8 h-8 rounded-full" />
                <span className="hidden sm:inline">{user.name}</span>
              </Link>
              <button
                onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
                className="text-sm text-gray-500 hover:text-red-500 border border-gray-300 rounded-full px-3 py-1"
              >
                Salir
              </button>
            </>
          ) : (
            <button
              onClick={() => loginWithRedirect()}
              className="bg-blue-500 hover:bg-blue-600 text-white text-sm font-semibold rounded-full px-4 py-1.5"
            >
              Iniciar sesión
            </button>
          )}
        </div>
      </div>
    </nav>
  );
}
