-- Sample mystery packages for development
INSERT INTO mystery_packages (id, title, description, image_path, price, currency, duration_minutes, min_players, max_players, difficulty, themes, plot_summary, is_available) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Murder and Dragons', 'A fantasy-themed murder mystery where players must solve the murder of a powerful sorcerer at a magical academy.', '/images/murder-and-dragons.png', 29.99, 'USD', 120, 6, 20, 'MEDIUM', '["Fantasy","Political Intrigue"]', 'In the enchanted kingdom of Eldoria, the Grand Sorcerer has been found murdered in his tower. As investigators from the Magical Council, players must navigate court politics, magical artifacts, and ancient rivalries to uncover the truth.', true),
('550e8400-e29b-41d4-a716-446655440002', 'Murder at the School of Witchcraft and Wizardry', 'A magical academy mystery with young wizards, enchanted artifacts, and dark secrets.', '/images/avada-kedavra.png', 19.99, 'USD', 120, 6, 16, 'EASY', '["Fantasy","Pop Culture"]', 'At the prestigious Hogwarts-style academy, the headmaster has been poisoned during the annual feast. Students and staff must investigate magical mishaps, forbidden spells, and hidden grudges.', true),
('550e8400-e29b-41d4-a716-446655440003', 'Murder at a Board Game Night', 'A meta murder mystery that breaks the fourth wall with game references and player interaction.', '/images/games-night.png', 19.99, 'USD', 120, 10, 24, 'MEDIUM', '["Humorous","Meta"]', 'During what should have been a friendly board game night, the host is found murdered. Players must navigate game mechanics, player personalities, and meta-narratives to solve the crime.', true);

-- Sample characters for first mystery package
INSERT INTO character_templates (mystery_package_id, name, description, avatar_path, background) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Elara Shadowbane', 'A cunning elf sorceress with a mysterious past.', '/images/countess.jpg', 'Elara is a master of shadow magic who graduated from the academy 10 years ago. She has been traveling the realms, collecting rare artifacts.'),
('550e8400-e29b-41d4-a716-446655440001', 'Thrain Ironfist', 'A dwarven blacksmith known for his craftsmanship and temper.', '/images/prince.jpg', 'Thrain has been the academy''s blacksmith for 30 years. His weapons and armor are legendary throughout the kingdom.'),
('550e8400-e29b-41d4-a716-446655440001', 'Detective Mira Voss', 'A human detective from the capital city, known for solving impossible cases.', '/images/wizard.jpg', 'Mira has solved over 50 murder cases in her career. She was personally requested by the king to investigate this sensitive matter.');

-- Sample game phases for first mystery package
INSERT INTO game_phases (mystery_package_id, name, order_index, instructions, host_instructions, objectives_to_add, inventory_to_add, evidence_to_add, game_state_to_unlock) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'The Discovery', 1, 
    '["Gather in the main hall", "Listen to the host''s announcement", "Begin asking initial questions"]',
    '["Describe the scene dramatically", "Distribute initial clues", "Answer questions about the academy"]',
    '[{"id": "obj1", "description": "Investigate the scene thoroughly", "targetGuestIds": []}]',
    '[]',
    '[{"id": "ev1", "name": "Bloody Dagger", "description": "A ceremonial dagger with fresh blood", "imagePath": "/images/bloody-dagger.jpg"}]',
    '["OBJECTIVES", "EVIDENCE"]'),
('550e8400-e29b-41d4-a716-446655440001', 'Individual Investigations', 2,
    '["Form small investigation groups", "Visit assigned locations", "Collect clues and interview suspects"]',
    '["Guide players to different areas", "Provide location-specific clues", "Facilitate role-playing interactions"]',
    '[{"id": "obj2", "description": "Search your assigned area thoroughly", "targetGuestIds": []}]',
    '[{"id": "inv1", "name": "Magnifying Glass", "description": "A detective''s magnifying glass for examining evidence", "imagePath": "/images/magnifying-glass.jpg", "quantity": 1, "targetGuestIds": []}]',
    '[{"id": "ev2", "name": "Torn Letter", "description": "A letter torn in half, mentions a secret meeting", "imagePath": "/images/torn-letter.jpg"}]',
    '["INVENTORY"]'),
('550e8400-e29b-41d4-a716-446655440001', 'The Accusation', 3,
    '["Present findings to the group", "Vote on suspects", "Defend your theories"]',
    '["Moderate the discussion", "Reveal additional evidence based on progress", "Build tension and suspense"]',
    '[{"id": "obj3", "description": "Make your final accusation with evidence to support it", "targetGuestIds": []}]',
    '[]',
    '[]',
    '[]'),
('550e8400-e29b-41d4-a716-446655440001', 'The Resolution', 4,
    '["Listen to the final revelation", "Discuss what you learned", "Reflect on the experience"]',
    '["Reveal the full story", "Explain any remaining mysteries", "Facilitate post-game discussion"]',
    '[]',
    '[]',
    '[]',
    '["SOLUTION"]');
