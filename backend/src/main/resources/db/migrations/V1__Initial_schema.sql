-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    avatar_path TEXT,
    is_host BOOLEAN DEFAULT FALSE,
    preferences TEXT DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Mystery packages table
CREATE TABLE mystery_packages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    image_path TEXT,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) DEFAULT 'USD',
    duration_minutes INTEGER NOT NULL,
    min_players INTEGER NOT NULL,
    max_players INTEGER NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    themes TEXT,
    plot_summary TEXT,
    is_available BOOLEAN DEFAULT TRUE
);

-- Characters table (templates)
CREATE TABLE character_templates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mystery_package_id UUID REFERENCES mystery_packages(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    avatar_path TEXT,
    background TEXT
);

-- Game phases table
CREATE TABLE game_phases (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mystery_package_id UUID REFERENCES mystery_packages(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INTEGER NOT NULL,
    duration_minutes INTEGER,
    instructions TEXT,
    host_instructions TEXT,
    is_interactive BOOLEAN DEFAULT FALSE,
    requires_host_action BOOLEAN DEFAULT FALSE
);

-- Create indexes for better performance
CREATE INDEX idx_mystery_packages_difficulty ON mystery_packages(difficulty);
CREATE INDEX idx_mystery_packages_available ON mystery_packages(is_available);
CREATE INDEX idx_character_templates_package_id ON character_templates(mystery_package_id);
CREATE INDEX idx_game_phases_package_id ON game_phases(mystery_package_id);
