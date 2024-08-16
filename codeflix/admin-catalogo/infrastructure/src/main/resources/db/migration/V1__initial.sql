create table category(
    id varchar(36) not null primary key,
    name varchar(255) not null,
    description varchar(4000),
    active BOOLEAN not null default true,
    created_at DATETIME(6) not null,
    updated_at DATETIME(6) not null,
    deleted_at DATETIME(6)
);