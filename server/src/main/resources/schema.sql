DROP TABLE IF EXISTS users, items, bookings, requests, comments;

CREATE TABLE IF NOT EXISTS  users(
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL UNIQUE,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests(
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(1024),
    requestor_Id INTEGER REFERENCES USERS(id) ON DELETE CASCADE NOT NULL,
    created TimeStamp NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items(
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    available BOOLEAN NOT NULL,
    owner_id INTEGER REFERENCES USERS(id) ON DELETE CASCADE NOT NULL,
    request_id INTEGER,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings(
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_Time TimeStamp NOT NULL,
    end_Time TimeStamp NOT NULL,
    status VARCHAR(20) NOT NULL,	
    booker_id INTEGER REFERENCES USERS(id) ON DELETE CASCADE NOT NULL,
    item_id INTEGER REFERENCES Items(id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments(
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(1024),
    commentor_id INTEGER REFERENCES USERS(id) ON DELETE CASCADE NOT NULL,
    item_id INTEGER REFERENCES Items(id) ON DELETE CASCADE NOT NULL,
    created TimeStamp NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);








