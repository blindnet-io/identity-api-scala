alter table accounts
    add email text not null default '',
    add password text not null default '',
    add token text not null default '';

create unique index on accounts (email);

alter table accounts
    alter email drop default,
    alter password drop default,
    alter token drop default;
