ALTER TABLE FILE_STORAGE ADD COLUMN LICENSE VARCHAR(50) AFTER LENGTH;

UPDATE FILE_STORAGE f set LICENSE='Creative Commons' where RECORDID in ('(BISIS)76908',
                                                                        '(BISIS)69013',
                                                                        '(BISIS)68982',
                                                                        '(BISIS)76735',
                                                                        '(BISIS)77263',
                                                                        '(BISIS)77591',
                                                                        '(BISIS)82079',
                                                                        '(BISIS)6742',
                                                                        '(BISIS)6381',
                                                                        '(BISIS)6429');