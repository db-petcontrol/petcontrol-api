ALTER TABLE tb_pet_tags
    DROP CONSTRAINT IF EXISTS fk_pet_id;

ALTER TABLE tb_pet_tags
    ADD CONSTRAINT fk_pet_id
        FOREIGN KEY (pet_id)
        REFERENCES tb_pets(id)
        ON DELETE CASCADE;
