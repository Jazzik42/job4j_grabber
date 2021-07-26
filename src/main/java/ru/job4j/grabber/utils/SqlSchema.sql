create table post (
id serial primary key,
title text,
link text not null unique,
text text,
created timestamp
)