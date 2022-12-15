create table accounts (
  id uuid primary key
);

create table application_groups (
  id uuid primary key,
  accid uuid not null,
  name text not null,
  key  text not null,
  constraint account_fk
    foreign key (accid)
    references accounts(id)
    on delete cascade
);

alter table applications add gid uuid not null;
alter table applications
  add constraint groupd_fk
    foreign key (gid)
    references application_groups(id)
    on delete cascade;
alter table applications drop key;
