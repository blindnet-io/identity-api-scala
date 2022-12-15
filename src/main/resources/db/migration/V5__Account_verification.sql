alter table accounts
    add verified boolean not null default false,
    add email_token text not null default '';

alter table accounts
    alter email_token drop default;
