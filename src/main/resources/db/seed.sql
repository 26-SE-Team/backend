SET search_path = public;

INSERT INTO users (user_id, social_type, google_email, kakao_email, name, phone, role, is_deleted, created_at)
VALUES
    (1, 'GOOGLE', 'admin@stayview.local', NULL, 'Admin User', '010-0000-0001', 'ADMIN', FALSE, NOW()),
    (2, 'GOOGLE', 'agent@stayview.local', NULL, 'Approved Agent', '010-0000-0002', 'USER', FALSE, NOW()),
    (3, 'KAKAO', NULL, 'tenant@stayview.local', 'Demo Tenant', '010-0000-0003', 'USER', FALSE, NOW())
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO agent_profiles (user_id, license_no, verification_status, created_at)
VALUES
    (2, 'AGENT-DEMO-0001', 'APPROVED', NOW())
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO spaces (
    space_id,
    agent_id,
    title,
    address,
    area,
    deposit,
    monthly_rent,
    maintenance_fee,
    room_type,
    available_date,
    status,
    living_environment_info,
    created_at
)
VALUES
    (
        1,
        2,
        'StayView Demo Studio',
        'Seoul Mapo-gu Demo-ro 12',
        24.5,
        1000,
        65,
        8,
        'STUDIO',
        CURRENT_DATE + INTERVAL '14 days',
        'AVAILABLE',
        'Near subway, convenience stores, and university district.',
        NOW()
    )
ON CONFLICT (space_id) DO NOTHING;

INSERT INTO space_images (image_id, space_id, image_order, image_url)
VALUES
    (1, 1, 1, 'https://example.com/images/stayview-demo-1.jpg'),
    (2, 1, 2, 'https://example.com/images/stayview-demo-2.jpg')
ON CONFLICT (image_id) DO NOTHING;

INSERT INTO favorites (favorite_id, user_id, space_id, created_at)
VALUES
    (1, 3, 1, NOW())
ON CONFLICT (favorite_id) DO NOTHING;

INSERT INTO chat_rooms (chat_room_id, space_id, tenant_id, created_at)
VALUES
    (1, 1, 3, NOW())
ON CONFLICT (chat_room_id) DO NOTHING;

INSERT INTO chat_messages (message_id, chat_room_id, sender_id, message, sent_at)
VALUES
    (1, 1, 3, 'Hello, is this room still available?', NOW() - INTERVAL '1 hour'),
    (2, 1, 2, 'Yes, it is available for viewing this week.', NOW() - INTERVAL '50 minutes')
ON CONFLICT (message_id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('users', 'user_id'), COALESCE((SELECT MAX(user_id) FROM users), 1), TRUE);
SELECT setval(pg_get_serial_sequence('spaces', 'space_id'), COALESCE((SELECT MAX(space_id) FROM spaces), 1), TRUE);
SELECT setval(pg_get_serial_sequence('space_images', 'image_id'), COALESCE((SELECT MAX(image_id) FROM space_images), 1), TRUE);
SELECT setval(pg_get_serial_sequence('favorites', 'favorite_id'), COALESCE((SELECT MAX(favorite_id) FROM favorites), 1), TRUE);
SELECT setval(pg_get_serial_sequence('chat_rooms', 'chat_room_id'), COALESCE((SELECT MAX(chat_room_id) FROM chat_rooms), 1), TRUE);
SELECT setval(pg_get_serial_sequence('chat_messages', 'message_id'), COALESCE((SELECT MAX(message_id) FROM chat_messages), 1), TRUE);
