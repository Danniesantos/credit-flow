CREATE TABLE credits (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    requested_amount NUMERIC(15,2) NOT NULL,
    credit_type VARCHAR NOT NULL,
    interest_rate NUMERIC(15,6) NOT NULL,
    status VARCHAR NOT NULL,
    payment_method VARCHAR,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_credit_customer
            FOREIGN KEY (customer_id)
            REFERENCES customers(id)
);