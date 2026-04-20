import { useState } from 'react';

export default function PostCard({ post, canRemove, onRemove }) {
  const [removing, setRemoving] = useState(false);

  async function handleRemove() {
    if (!window.confirm('¿Eliminar esta publicación del stream?')) return;
    setRemoving(true);
    try {
      await onRemove();
    } finally {
      setRemoving(false);
    }
  }

  const displayName = post.userEmail ?? post.userId ?? 'Anónimo';
  const initial = displayName.charAt(0).toUpperCase();

  return (
    <div className="bg-white border border-gray-200 rounded-xl p-4 hover:bg-gray-50 transition">
      <div className="flex items-start gap-3">
        <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-500 font-bold shrink-0">
          {initial}
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center justify-between gap-2">
            <span className="font-semibold text-gray-900 text-sm truncate">{displayName}</span>
            {canRemove && (
              <button
                onClick={handleRemove}
                disabled={removing}
                className="text-xs text-gray-400 hover:text-red-500 disabled:opacity-50 shrink-0"
              >
                {removing ? '...' : 'Eliminar'}
              </button>
            )}
          </div>
          <p className="text-gray-800 mt-1 break-words">{post.message}</p>
        </div>
      </div>
    </div>
  );
}
