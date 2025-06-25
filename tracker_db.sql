drop database if exists tracker_db;

create database tracker_db;

use tracker_db;

create table
  users (
    user_id int auto_increment primary key,
    clearance int not null default 0,
    name varchar(255) not null,
    password varchar(255) not null
  );

create table
  books (
    book_id int auto_increment primary key,
    title varchar(255) not null,
    author varchar(255) not null,
    num_pages int not null
  );

create table
  trackers (
    user_id int not null,
    book_id int not null,
    progress int not null default 0,
    rating int,
    primary key (user_id, book_id),
    foreign key (user_id) references users (user_id),
    foreign key (book_id) references books (book_id)
  );

-- Insert books
INSERT INTO
  books (title, author, num_pages)
VALUES
  (
    'Thus Spoke Zarathustra',
    'Friedrich Nietzsche',
    327
  );

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('The Republic', 'Plato', 416);

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('Nicomachean Ethics', 'Aristotle', 400);

INSERT INTO
  books (title, author, num_pages)
VALUES
  (
    'Meditations on First Philosophy',
    'René Descartes',
    80
  );

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('Apology', 'Socrates', 40);

INSERT INTO
  books (title, author, num_pages)
VALUES
  (
    'Beyond Good and Evil',
    'Friedrich Nietzsche',
    320
  );

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('Critique of Pure Reason', 'Immanuel Kant', 856);

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('Being and Time', 'Martin Heidegger', 589);

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('The Symposium', 'Plato', 80);

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('The Prince', 'Niccolò Machiavelli', 140);

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('Leviathan', 'Thomas Hobbes', 736);

INSERT INTO
  books (title, author, num_pages)
VALUES
  (
    'The Social Contract',
    'Jean-Jacques Rousseau',
    192
  );

INSERT INTO
  books (title, author, num_pages)
VALUES
  (
    'Tractatus Logico-Philosophicus',
    'Ludwig Wittgenstein',
    232
  );

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('Phenomenology of Spirit', 'G. W. F. Hegel', 640);

INSERT INTO
  books (title, author, num_pages)
VALUES
  ('On Liberty', 'John Stuart Mill', 176);