UPDATE credits
SET installments_quantity = 1
WHERE installments_quantity IS NULL;

ALTER TABLE credits
ALTER COLUMN installments_quantity
SET NOT NULL;