const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export async function apiRequest(path, options = {}, userId) {
  const headers = {
    'Content-Type': 'application/json',
    ...(userId && { 'X-User-Id': userId }),
    ...options.headers,
  };
  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });
  if (!res.ok && res.status !== 204) {
    const errorText = await res.text();
    throw new Error(errorText || `HTTP ${res.status}`);
  }
  if (res.status === 204) return null;
  const text = await res.text();
  if (!text) return null;
  return JSON.parse(text);
}

export const readingListApi = {
  async list(userId) {
    const [unread, read] = await Promise.all([
      apiRequest('/api/reading-list?status=UNREAD', { method: 'GET' }, userId),
      apiRequest('/api/reading-list?status=READ', { method: 'GET' }, userId),
    ]);

    const normalize = (arr) =>
      (Array.isArray(arr) ? arr : []).map((item) => ({
        ...item,
        id: item.bookId,
        readStatus: item.status === 'READ',
      }));

    return [...normalize(unread), ...normalize(read)];
  },

  create(userId, body) {
    const dto = {
      title: body.title,
      author: body.author,
      notes: body.notes ?? null,
      status: body.readStatus ? 'READ' : 'UNREAD',
    };
    return apiRequest('/api/reading-list', { method: 'POST', body: JSON.stringify(dto) }, userId);
  },

  update(userId, bookId, body) {
    const dto = {
      bookId,
      title: body.title,
      author: body.author,
      notes: body.notes ?? null,
      status: body.readStatus ? 'READ' : 'UNREAD',
    };
    
    return apiRequest('/api/reading-list', { method: 'POST', body: JSON.stringify(dto) }, userId);
  },

  delete(userId, bookId) {
    return apiRequest(`/api/reading-list/${bookId}`, { method: 'DELETE' }, userId);
  },
};
