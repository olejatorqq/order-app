CREATE TABLE IF NOT EXISTS public.users (
                                            id BIGSERIAL PRIMARY KEY,
                                            username VARCHAR(255) NOT NULL UNIQUE,
                                            email VARCHAR(255) NOT NULL UNIQUE,
                                            password VARCHAR(255) NOT NULL,
                                            created_at TIMESTAMP NOT NULL
);