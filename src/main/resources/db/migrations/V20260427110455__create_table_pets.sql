CREATE TABLE IF NOT EXISTS tb_pets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    species_id UUID NOT NULL,
    CONSTRAINT fk_pet_species FOREIGN KEY (species_id) REFERENCES tb_species(id)
);
