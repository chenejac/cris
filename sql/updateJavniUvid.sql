INSERT INTO FILE_STORAGE (RECORDID, FILENAME, MIME, UPLOADER, LENGTH, DATEMODIFIED, LICENSE, TYPE, NOTE)
SELECT RECORDID, 'ju' + FILENAME, MIME, UPLOADER, LENGTH, DATEMODIFIED, LICENSE, TYPE, NOTE
FROM FILE_STORAGE
WHERE DATEMODIFIED is not null