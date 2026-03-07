import React from 'react';
import { useDraggable } from '@dnd-kit/core';
import { CSS } from '@dnd-kit/utilities';
import './BookCard.css';

export default function BookCard({ book }) {
  const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({
    id: String(book.id),
    data: { book },
  });

  const style = transform
    ? { transform: CSS.Translate.toString(transform) }
    : undefined;

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={`book-card ${isDragging ? 'book-card--dragging' : ''}`}
      {...listeners}
      {...attributes}
    >
      <div className="book-card-title">{book.title}</div>
      <div className="book-card-author">{book.author}</div>
      {book.notes && <div className="book-card-notes">{book.notes}</div>}
    </div>
  );
}
