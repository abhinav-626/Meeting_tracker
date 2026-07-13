CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE meetings (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          title VARCHAR(255) NOT NULL,
                          meeting_date TIMESTAMP NOT NULL,
                          raw_transcript TEXT NOT NULL,
                          source VARCHAR(50) NOT NULL DEFAULT 'MANUAL_PASTE',
                          series_id UUID,                     -- groups recurring meetings for carry-forward logic
                          created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE action_items (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              meeting_id UUID NOT NULL REFERENCES meetings(id) ON DELETE CASCADE,
                              description TEXT NOT NULL,
                              owner_id UUID REFERENCES users(id),
                              deadline DATE,
                              confidence_score DECIMAL(3,2) NOT NULL,   -- 0.00 to 1.00
                              status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
                              carried_forward_count INT NOT NULL DEFAULT 0,
                              created_at TIMESTAMP NOT NULL DEFAULT now(),
                              updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_action_items_meeting ON action_items(meeting_id);
CREATE INDEX idx_action_items_owner ON action_items(owner_id);
CREATE INDEX idx_meetings_series ON meetings(series_id);