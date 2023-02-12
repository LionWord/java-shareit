INSERT INTO users (user_name, user_email)
values ('Igor', 'wow@uff.com');
INSERT INTO users (user_name, user_email)
values ('Sasha', 'drish@nomuscles.com');

INSERT INTO items (item_name, item_description, item_is_available, owner_id, request_id)
values ('drill', 'super drill', true, 1, 0);

INSERT INTO bookings(start_date, end_date, item_id, booker_id, status)
values ('2020-06-22 19:10:25+6', '2021-06-22 19:10:25+6', 1, 2, 2)