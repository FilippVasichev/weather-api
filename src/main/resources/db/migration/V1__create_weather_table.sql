CREATE TABLE IF NOT EXISTS "weather" (
    id SERIAL PRIMARY KEY,
    city TEXT NOT NULL,
    country TEXT NOT NULL,
    temperature INTEGER NOT NULL,
    date DATE
);