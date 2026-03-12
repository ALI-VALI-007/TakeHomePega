import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  DndContext,
  DragOverlay,
  useDroppable,
} from '@dnd-kit/core';
import { useAuth } from '../context/AuthContext';
import { readingListApi } from '../api/client';
import BookCard from '../components/BookCard';
import AddBookModal from '../components/AddBookModal';
import './ReadingListPage.css';

const LIST_WANT = 'want';
const LIST_READ = 'read';
const TRASH = 'trash';

function DroppableColumn({ id, title, books, onBookClick }) {
  const { setNodeRef, isOver } = useDroppable({ id });

  return (
    <div
      ref={setNodeRef}
      className={`reading-column ${isOver ? 'reading-column--over' : ''}`}
    >
      <h2 className="reading-column-title">{title}</h2>
      <div className="reading-column-list">
        {books.length === 0 && (
          <div className="reading-column-empty">Drop books here</div>
        )}
        {books.map((book) => (
          <BookCard key={book.id} book={book} onEdit={onBookClick} />
        ))}
      </div>
    </div>
  );
}

function TrashDropZone() {
  const { setNodeRef, isOver } = useDroppable({ id: TRASH });

  return (
    <div
      ref={setNodeRef}
      className={`reading-trash ${isOver ? 'reading-trash--over' : ''}`}
    >
      🗑 Delete
    </div>
  );
}

export default function ReadingListPage() {
  const { userId, canUseApp, signOut, loading } = useAuth();
  const navigate = useNavigate();
  const [items, setItems] = useState([]);
  const [loadingList, setLoadingList] = useState(true);
  const [error, setError] = useState('');
  const [addModalOpen, setAddModalOpen] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [activeBook, setActiveBook] = useState(null);

  const fetchList = useCallback(async () => {
    if (!userId) return;
    setLoadingList(true);
    setError('');
    try {
      const list = await readingListApi.list(userId);
      setItems(Array.isArray(list) ? list : []);
    } catch (err) {
      setError(err.message || 'Failed to load list');
      setItems([]);
    } finally {
      setLoadingList(false);
    }
  }, [userId]);

  useEffect(() => {
    if (!canUseApp) {
      navigate('/login', { replace: true });
      return;
    }
    fetchList();
  }, [canUseApp, userId, navigate, fetchList]);

  const wantToRead = items.filter((b) => !b.readStatus);
  const read = items.filter((b) => b.readStatus);

  const handleEditBookClick = (book) => {
    setEditingBook(book);
    setAddModalOpen(true);
  };

  const handleDragStart = (event) => {
    const book = event.active.data.current?.book;
    if (book) setActiveBook(book);
  };

  const handleDragEnd = async (event) => {
    setActiveBook(null);
    const { active, over } = event;
    if (!over || !userId) return;

    const book = active.data.current?.book;
    const overId = String(over.id);

    if (!book) return;

    if (overId === TRASH) {
      try {
        await readingListApi.delete(userId, book.id);
        await fetchList();
      } catch (err) {
        setError(err.message || 'Failed to delete');
      }
      return;
    }

    let targetRead;
    if (overId === LIST_READ) targetRead = true;
    else if (overId === LIST_WANT) targetRead = false;
    else {
      const overBook = items.find((b) => String(b.id) === overId);
      targetRead = overBook ? overBook.readStatus : null;
    }
    if (targetRead === undefined || targetRead === null) return;
    if (targetRead === book.readStatus) return;

    try {
      await readingListApi.update(userId, book.id, {
        ...book,
        readStatus: targetRead,
      });
      await fetchList();
    } catch (err) {
      setError(err.message || 'Failed to update');
    }
  };

  const handleAddBook = async (body) => {
    await readingListApi.create(userId, body);
    await fetchList();
  };

  if (loading || !userId) {
    return (
      <div className="reading-page">
        <div className="reading-loading">Loading…</div>
      </div>
    );
  }

  return (
    <div className="reading-page">
      <header className="reading-header">
        <h1>Reading List</h1>
        <div className="reading-header-actions">
          <button type="button" className="btn btn-primary" onClick={() => setAddModalOpen(true)}>
            Add book
          </button>
          <button type="button" className="btn btn-ghost" onClick={() => signOut().then(() => navigate('/login'))}>
            Sign out
          </button>
        </div>
      </header>

      {error && (
        <div className="reading-error">
          {error}
          <button type="button" onClick={() => setError('')}>Dismiss</button>
        </div>
      )}

      {loadingList ? (
        <div className="reading-loading">Loading your books…</div>
      ) : (
        <DndContext
          onDragStart={handleDragStart}
          onDragEnd={handleDragEnd}
        >
          <div className="reading-columns">
            <DroppableColumn
              id={LIST_WANT}
              title="Want to read"
              books={wantToRead}
              onBookClick={handleEditBookClick}
            />
            <DroppableColumn
              id={LIST_READ}
              title="Read"
              books={read}
              onBookClick={handleEditBookClick}
            />
          </div>

          <TrashDropZone />

          <DragOverlay>
            {activeBook ? (
              <div className="book-card-overlay">
                <BookCard book={activeBook} />
              </div>
            ) : null}
          </DragOverlay>
        </DndContext>
      )}

      {addModalOpen && (
        <AddBookModal
          onClose={() => {
            setAddModalOpen(false);
            setEditingBook(null);
          }}
          onSubmit={editingBook
            ? async (values) => {
                await readingListApi.update(userId, editingBook.id, { ...editingBook, ...values });
                await fetchList();
              }
            : handleAddBook}
          initialBook={editingBook}
        />
      )}
    </div>
  );
}
