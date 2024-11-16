CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        product VARCHAR(255) NOT NULL,
                        quantity INT NOT NULL,
                        price DOUBLE PRECISION NOT NULL,
                        order_date TIMESTAMP NOT NULL
);