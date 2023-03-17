CREATE TABLE IF NOT EXISTS CARDS (
    --id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    number BIGINT NOT NULL,
    country VARCHAR(50),
    city VARCHAR(50),
    url VARCHAR(50),
    phone VARCHAR(20),
    last_request TIMESTAMP NOT NULL,
    CONSTRAINT PK_CARD PRIMARY KEY (number)
    );