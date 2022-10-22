CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    email varchar(100) NOT NULL,
    CONSTRAINT uniq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS locations (
    location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    lat real NOT NULL,
    lon real NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT uniq_name_category UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    title VARCHAR(100) NOT NULL,
    annotation VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    category_id BIGINT REFERENCES categories(category_id) NOT NULL,
    confirmed_requests BIGINT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT REFERENCES users(user_id) NOT NULL,
    location_id BIGINT REFERENCES locations(location_id) NOT NULL,
    is_paid BOOLEAN NOT NULL,
    participant_limit REAL NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    is_request_moderation BOOLEAN NOT NULL,
    state VARCHAR(10) NOT NULL,
    views BIGINT NOT NULL,
    CONSTRAINT uniq_event_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    title VARCHAR(100) NOT NULL,
    is_pinned BOOLEAN NOT NULL,
    CONSTRAINT uniq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS event_compilations (
    compilation_id BIGINT REFERENCES compilations(compilation_id) NOT NULL,
    event_id BIGINT REFERENCES events(event_id) NOT NULL,
    PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    event_id BIGINT REFERENCES events(event_id) NOT NULL,
    requester_id BIGINT REFERENCES users(user_id) NOT NULL,
    status VARCHAR(10),
    CONSTRAINT uniq_event_requester UNIQUE (event_id, requester_id)
);