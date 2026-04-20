import { useEffect, useState, useCallback } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import PostCard from '../components/PostCard';
import PostForm from '../components/PostForm';
import { useApi } from '../hooks/useApi';

export default function Feed() {
  const { isAuthenticated, isLoading } = useAuth0();
  const { getStream, getAllPosts, removeFromStream } = useApi();
  const [posts, setPosts] = useState([]);
  const [fetching, setFetching] = useState(true);
  const [fetchError, setFetchError] = useState('');

  const loadFeed = useCallback(async () => {
    setFetching(true);
    setFetchError('');
    try {
      const [stream, allPosts] = await Promise.all([getStream(), getAllPosts()]);

      const postsMap = Object.fromEntries(
        (Array.isArray(allPosts) ? allPosts : []).map((p) => [p.postId, p])
      );

      const ordered = (stream.postsId ?? [])
        .map((id) => postsMap[id])
        .filter(Boolean);

      setPosts(ordered);
    } catch {
      setFetchError('Error al cargar el feed.');
    } finally {
      setFetching(false);
    }
  }, [getStream, getAllPosts]);

  useEffect(() => {
    loadFeed();
  }, [loadFeed]);

  async function handleRemove(postId) {
    try {
      await removeFromStream(postId);
      loadFeed();
    } catch {
      // silently ignore
    }
  }

  if (isLoading) {
    return <p className="text-center text-gray-400 mt-10">Cargando...</p>;
  }

  return (
    <div>
      <h1 className="text-xl font-bold text-gray-900 mb-4">Feed Público</h1>

      {isAuthenticated && <PostForm onPostCreated={loadFeed} />}

      {fetching && <p className="text-center text-gray-400 mt-6">Cargando publicaciones...</p>}
      {fetchError && <p className="text-center text-red-400 mt-6">{fetchError}</p>}

      {!fetching && posts.length === 0 && !fetchError && (
        <p className="text-center text-gray-400 mt-6">No hay publicaciones aún. ¡Sé el primero!</p>
      )}

      <div className="flex flex-col gap-3">
        {posts.map((post) => (
          <PostCard
            key={post.postId}
            post={post}
            canRemove={isAuthenticated}
            onRemove={() => handleRemove(post.postId)}
          />
        ))}
      </div>
    </div>
  );
}
