-- tables
CREATE TABLE threads (
  id    BIGINT PRIMARY KEY AUTO_INCREMENT,
  url   VARCHAR(256) NOT NULL,
  title VARCHAR(512) NULL
);

CREATE TABLE comments (
  id                     BIGINT PRIMARY KEY AUTO_INCREMENT,
  thread_id              BIGINT       NOT NULL,
  parent_id              BIGINT       NULL,
  creation_date          TIMESTAMP    NOT NULL,
  last_modification_date TIMESTAMP    NOT NULL,
  status                 VARCHAR(32)  NOT NULL,
  text                   TEXT         NOT NULL,
  author                 VARCHAR(128) NULL,
  email_hash             CHAR(32)     NULL,
  url                    VARCHAR(256) NULL,
  FOREIGN KEY (thread_id) REFERENCES threads (id),
  FOREIGN KEY (parent_id) REFERENCES comments (id)
);

-- indexes
CREATE INDEX thread_id_idx
  ON threads (id);
CREATE INDEX thread_url_idx
  ON threads (url);

CREATE INDEX comment_id_idx
  ON comments (id);
CREATE INDEX comment_thread_id_idx
  ON comments (thread_id);
CREATE INDEX comment_creation_date_idx
  ON comments (creation_date);
CREATE INDEX comment_status_idx
  ON comments (status);
