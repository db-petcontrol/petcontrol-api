CREATE TABLE IF NOT EXISTS tb_pet_tags (
    pet_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (pet_id, tag_id),
    CONSTRAINT fk_pet_id FOREIGN KEY (pet_id) REFERENCES tb_pets(id),
    CONSTRAINT fk_tag_id FOREIGN KEY (tag_id) REFERENCES tb_tags(id)
);