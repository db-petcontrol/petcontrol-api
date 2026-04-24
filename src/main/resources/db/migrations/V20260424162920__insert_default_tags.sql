INSERT INTO tb_tags (name) VALUES
('adaptado a apartamento'),
('amigável'),
('castrado'),
('independente'),
('necessita de cuidados especiais'),
('sociável com outros animais'),
('treinado'),
('vacinado'),
('vermifugado')
ON CONFLICT (name) DO NOTHING;