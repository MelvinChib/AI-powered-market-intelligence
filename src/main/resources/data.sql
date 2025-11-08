-- Create tables
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ANALYST'
);

CREATE TABLE IF NOT EXISTS agent_insights (
    id SERIAL PRIMARY KEY,
    agent_name VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    sentiment VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS compliance_logs (
    id SERIAL PRIMARY KEY,
    agent VARCHAR(100) NOT NULL,
    rule VARCHAR(255) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO users (username, password, role) VALUES 
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ADMIN');
INSERT INTO users (username, password, role) VALUES 
('analyst', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ANALYST');
INSERT INTO users (username, password, role) VALUES 
('viewer', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'VIEWER');

INSERT INTO agent_insights (agent_name, message, sentiment) VALUES 
('Market Analyzer', 'Strong bullish momentum detected in tech sector', 'POSITIVE');
INSERT INTO agent_insights (agent_name, message, sentiment) VALUES 
('Risk Monitor', 'Elevated volatility in energy stocks requires attention', 'NEGATIVE');
INSERT INTO agent_insights (agent_name, message, sentiment) VALUES 
('Trade Executor', 'Successfully executed 15 trades with 87% accuracy', 'POSITIVE');