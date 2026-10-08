-- SQL Migration Script for Order Tracking System
-- Run this script to update the database schema

-- Add tracking table if it doesn't exist
CREATE TABLE IF NOT EXISTS order_tracking (
    tracking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (order_id) REFERENCES place_orders(order_id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add status column to place_orders if it doesn't exist
ALTER TABLE place_orders ADD COLUMN IF NOT EXISTS status VARCHAR(50) NOT NULL DEFAULT 'PENDING';
ALTER TABLE place_orders ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NULL;
ALTER TABLE place_orders ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP NULL;

-- Update existing timestamps if null
UPDATE place_orders SET created_at = NOW() WHERE created_at IS NULL;
UPDATE place_orders SET updated_at = NOW() WHERE updated_at IS NULL;

-- Add indexes for better query performance
ALTER TABLE place_orders ADD INDEX IF NOT EXISTS idx_email (email);
ALTER TABLE place_orders ADD INDEX IF NOT EXISTS idx_phone (phoneNumber);
ALTER TABLE place_orders ADD INDEX IF NOT EXISTS idx_status (status);
ALTER TABLE place_orders ADD INDEX IF NOT EXISTS idx_created_at (created_at);

-- Create initial tracking records for existing orders
INSERT INTO order_tracking (order_id, status, description)
SELECT order_id, status, CONCAT('Order created with status: ', status)
FROM place_orders
WHERE NOT EXISTS (
    SELECT 1 FROM order_tracking WHERE order_tracking.order_id = place_orders.order_id
);
