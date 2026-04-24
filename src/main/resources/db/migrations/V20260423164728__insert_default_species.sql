INSERT INTO tb_species (name) VALUES
('Calopsita'),
('Cachorro'),
('Canário'),
('Coelho'),
('Gato'),
('Hamster'),
('Peixe'),
('Porquinho-da-índia')
ON CONFLICT (name) DO NOTHING;