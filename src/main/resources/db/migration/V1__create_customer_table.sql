CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR(11) NOT NULL,
    monthly_income NUMERIC(15,2) NOT NULL,
    credit_score INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);