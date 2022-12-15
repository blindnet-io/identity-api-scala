drop index accounts_email_idx;

create unique index on accounts (email);
