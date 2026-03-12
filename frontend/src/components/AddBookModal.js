import React, { useState } from 'react';
import './AddBookModal.css';

export default function AddBookModal({ onClose, onSubmit, initialBook }) {
  const isEdit = !!initialBook;
  const [title, setTitle] = useState(initialBook?.title || '');
  const [author, setAuthor] = useState(initialBook?.author || '');
  const [notes, setNotes] = useState(initialBook?.notes || '');
  const [readStatus, setReadStatus] = useState(initialBook?.readStatus || false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      const payload = {
        title: title.trim(),
        author: author.trim(),
        notes: notes.trim() || null,
        readStatus,
      };
      await onSubmit(payload);
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to add book');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal add-book-modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{isEdit ? 'Edit book' : 'Add book'}</h2>
          <button type="button" className="modal-close" onClick={onClose} aria-label="Close">
            ×
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          {error && <div className="modal-error">{error}</div>}
          <label>
            Title
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Book title"
              required
            />
          </label>
          <label>
            Author
            <input
              type="text"
              value={author}
              onChange={(e) => setAuthor(e.target.value)}
              placeholder="Author name"
              required
            />
          </label>
          <label>
            Notes (optional)
            <textarea
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Notes…"
              rows={3}
            />
          </label>
          <label className="checkbox-label">
            <input
              type="checkbox"
              checked={readStatus}
              onChange={(e) => setReadStatus(e.target.checked)}
            />
            Mark as read
          </label>
          <div className="modal-actions">
            <button type="button" onClick={onClose}>Cancel</button>
            <button type="submit" disabled={submitting}>
              {submitting ? 'Adding…' : 'Add book'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
