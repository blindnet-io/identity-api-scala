alter table accounts
    add created_at timestamptz default now();
