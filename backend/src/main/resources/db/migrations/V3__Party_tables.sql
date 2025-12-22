-- Create parties table
CREATE TABLE parties (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    host_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mystery_package_id UUID NOT NULL REFERENCES mystery_packages(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    scheduled_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    max_guests INT NOT NULL,
    current_phase_index INT NOT NULL DEFAULT 0,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create guests table
CREATE TABLE guests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    party_id UUID NOT NULL REFERENCES parties(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    name VARCHAR(255) NOT NULL,
    invite_code VARCHAR(50) NOT NULL UNIQUE,
    character_id UUID,
    status VARCHAR(50) NOT NULL DEFAULT 'INVITED',
    joined_at TIMESTAMP
);

-- Create indexes for efficient queries
CREATE INDEX idx_parties_host ON parties(host_id);
CREATE INDEX idx_guests_party ON guests(party_id);
CREATE INDEX idx_guests_user ON guests(user_id);
CREATE INDEX idx_guests_invite_code ON guests(invite_code);
