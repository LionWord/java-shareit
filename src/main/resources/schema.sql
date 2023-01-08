create table if not exists users
(
    user_id    integer not null
        constraint "USERS_pkey"
            primary key,
    user_name  varchar not null,
    user_email varchar not null
);

create table if not exists items
(
    item_id           integer not null
        primary key,
    item_name         varchar not null,
    item_description  varchar,
    item_is_available boolean,
    owner_id          integer
        constraint "OWNER"
            references users,
    request_id        integer not null
);

create table if not exists bookings
(
    booking_id integer   not null
        primary key,
    start_date timestamp not null,
    end_date   timestamp not null,
    item_id    integer   not null
        constraint item
            references items,
    booker_id  integer   not null
        constraint booker
            references users,
    status     varchar   not null
);

create table if not exists requests
(
    request_id          integer not null
        primary key,
    request_description varchar,
    requestor_id        integer not null
        constraint requestor
            references users
);

create table if not exists comments
(
    comment_id   integer not null
        primary key,
    comment_text varchar not null,
    item_id      integer not null
        constraint item
            references items,
    author_id    integer not null
        constraint author
            references users
);


