-- V1__alter_file_metadata.sql

ALTER TABLE file_metadata
RENAME COLUMN user_id to owner_id;

ALTER TABLE file_metadata
ALTER COLUMN owner_id TYPE BIGINT USING owner_id::bigint;

ALTER TABLE file_metadata
ADD CONSTRAINT file_owner
FOREIGN KEY (owner_id) REFERENCES _user(id);