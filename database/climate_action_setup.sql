USE climate_action;

CREATE TABLE emission_factors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_type VARCHAR(50) NOT NULL,
    unit VARCHAR(30) NOT NULL,
    factor_value DECIMAL(12,6) NOT NULL,
    source VARCHAR(255)
);