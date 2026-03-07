import React, { useState } from 'react';
import './AddBookModal.css';

export default function AddBookModal({ onClose, onAdded }) {
  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [notes, setNotes] = useState('');
  const [readStatus, setReadStatus] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await onAdded({ title: title.trim(), author: author.trim(), notes: notes.trim() || null, readStatus });
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
          <h2>Add book</h2>
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
