INSERT INTO public."User"(
	id, "firstName", "lastName", "walletId")
	VALUES (-1, 'Google', 'Chrome', '0xAdCbd04E11E38E2d24bbAc874D3F4DcCc692C6Fc');

-- 1. Create a sequence
CREATE SEQUENCE transactions_id_seq;

-- 2. Set the default value of the id column to use the sequence
ALTER TABLE transactions
ALTER COLUMN id SET DEFAULT nextval('transactions_id_seq');

-- 3. (Optional) Set the sequence to the current max id, so there's no conflict
SELECT setval('transactions_id_seq', COALESCE((SELECT MAX(id) FROM transactions), 1));
