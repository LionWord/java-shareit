create table if not exists users
(
    user_id
    serial
    primary
    key,
    user_name
    varchar
(
    64
) not null,
    user_email varchar
(
    64
) not null
    unique
    );

create table if not exists items
(
    item_id
    serial
    primary
    key,
    item_name
    varchar
(
    64
) not null,
    item_description varchar
(
    256
),
    item_is_available boolean,
    owner_id integer
    constraint "OWNER"
    references users on UPDATE cascade
                     on delete cascade,
    request_id integer not null
    );

create table if not exists bookings
(
    booking_id
    serial
    primary
    key,
    start_date
    timestamp
    without
    time
    zone,
    end_date
    timestamp
    without
    time
    zone,
    item_id
    integer
    not
    null
    constraint
    "ITEM_BOOKING"
    references
    items
    on
    UPDATE
    cascade
    on
    delete
    cascade,
    booker_id
    integer
    not
    null
    constraint
    booker
    references
    users
    on
    UPDATE
    cascade
    on
    delete
    cascade,
    status
    varchar
    not
    null
);

create table if not exists comments
(
    comment_id
    serial
    primary
    key,
    comment_text
    varchar
(
    256
) not null,
    item_id integer not null
    constraint "ITEM_COMMENTS"
    references items on UPDATE cascade
                     on delete cascade,
    author_id integer not null
    constraint author
    references users
                     on UPDATE cascade
                     on delete cascade,
    created timestamp
                     without time zone
    );

create table if not exists requests
(
    request_id
    integer
    generated
    by
    default as
    identity
    primary
    key,
    request_text
    varchar
(
    256
),
    request_created timestamp without time zone,
    requester_id integer
    );

create table if not exists responses
(
    response_id
    integer
    generated
    by
    default as
    identity
    primary
    key,
    request_id
    integer,
    item_id
    integer,
    unique
(
    request_id,
    item_id
)
    );

