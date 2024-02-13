CREATE TABLE IF NOT EXISTS transaction
(
    id uuid PRIMARY KEY,
    time timestamp WITH TIME ZONE,
    card_id uuid,
    category_id uuid,
    amount numeric
);
