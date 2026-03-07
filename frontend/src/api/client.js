const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export async function apiRequest(path, options = {}, userId) {
  const headers = {
    'Content-Type': 'application/json',
    ...(userId && { 'X-User-Id': userId }),
    ...options.headers,
  };
  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });
  if (!res.ok && res.status !== 204) {
    const text = await res.text();
    throw new Error(text || `HTTP ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export const readingListApi = {
  list(userId) {
    return apiRequest('/api/reading-list', { method: 'GET' }, userId);
  },
  get(userId, bookId) {
    return apiRequest(`/api/reading-list/${bookId}`, { method: 'GET' }, userId);
  },
  create(userId, body) {
    return apiRequest('/api/reading-list', { method: 'POST', body: JSON.stringify(body) }, userId);
  },
  update(userId, bookId, body) {
    return apiRequest(`/api/reading-list/${bookId}`, { method: 'PUT', body: JSON.stringify(body) }, userId);
  },
  delete(userId, bookId) {
    return apiRequest(`/api/reading-list/${bookId}`, { method: 'DELETE' }, userId);
  },
};
