CREATE TABLE notifications (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        message TEXT NOT NULL,
                        sent_at TIMESTAMP NOT NULL
);