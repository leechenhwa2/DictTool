CREATE TABLE Dict (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	word TEXT,
	content TEXT,
	word1 TEXT, word2 TEXT);

CREATE INDEX Dict_word_IDX ON Dict (word);
CREATE INDEX Dict_word1_IDX ON Dict (word1);
CREATE INDEX Dict_word2_IDX ON Dict (word2);