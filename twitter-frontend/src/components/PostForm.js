import { useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { useApi } from '../hooks/useApi';

const MAX_CHARS = 140;

export default function PostForm({ onPostCreated }) {
  const { user } = useAuth0();
  const { createPost } = useApi();
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const remaining = MAX_CHARS - message.length;

  async function handleSubmit(e) {
    e.preventDefault();
    if (!message.trim() || remaining < 0) return;

    setLoading(true);
    setError('');
    try {
      await createPost(message, user.sub, user.email);
      setMessage('');
      onPostCreated?.();
    } catch {
      setError('No se pudo crear la publicación. Inténtalo de nuevo.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="bg-white border border-gray-200 rounded-xl p-4 mb-4">
      <textarea
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        placeholder="¿Qué está pasando?"
        rows={3}
        maxLength={MAX_CHARS}
        className="w-full resize-none outline-none text-gray-800 placeholder-gray-400 text-base"
      />
      <div className="flex items-center justify-between mt-2 border-t border-gray-100 pt-2">
        <span className={`text-sm ${remaining < 20 ? 'text-red-500' : 'text-gray-400'}`}>
          {remaining}
        </span>
        {error && <span className="text-xs text-red-500">{error}</span>}
        <button
          type="submit"
          disabled={loading || !message.trim() || remaining < 0}
          className="bg-blue-500 hover:bg-blue-600 disabled:opacity-50 text-white text-sm font-semibold rounded-full px-5 py-1.5"
        >
          {loading ? 'Publicando...' : 'Publicar'}
        </button>
      </div>
    </form>
  );
}
