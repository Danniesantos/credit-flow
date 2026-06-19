CREATE TABLE installments (
    id UUID PRIMARY KEY,
    number INTEGER NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR NOT NULL,
    credit_id UUID NOT NULL,
    CONSTRAINT fk_installment_credit
           FOREIGN KEY (credit_id)
           REFERENCES credits(id)
);