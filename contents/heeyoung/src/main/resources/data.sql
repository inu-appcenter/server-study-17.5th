
-- 3. ID 없이 데이터 삽입 (Auto Increment 사용)
INSERT INTO `user` (login_id, password, email, phone_num, address, nickname, name)
VALUES
    ('heeyoung01', '1234', 'heeyoung01@example.com', '010-1111-2222', '서울특별시 강남구 테헤란로 1', '희영짱', '김희영'),
    ('minsu02', 'abcd', 'minsu02@example.com', '010-2222-3333', '서울특별시 마포구 월드컵북로 23', '민수킹', '이민수'),
    ('jina03', 'qwer', 'jina03@example.com', '010-3333-4444', '서울특별시 송파구 올림픽로 101', '지나쨩', '박지나');

INSERT INTO store (store_name, store_phone, min_price, store_address)
VALUES
    ('희영이네 분식', '02-111-2222', 5000, '서울특별시 강남구 테헤란로 12'),
    ('민수치킨', '02-222-3333', 15000, '서울특별시 마포구 서교동 101-1'),
    ('지나카페', '02-333-4444', 4000, '서울특별시 송파구 방이동 77-2');

INSERT INTO menu (menu_name, price, store_id) VALUES ('떡볶이', 4000, 1);
INSERT INTO menu (menu_name, price, store_id) VALUES ('김밥', 3000, 1);
INSERT INTO menu (menu_name, price, store_id) VALUES ('순대', 4000, 1);
INSERT INTO menu (menu_name, price, store_id) VALUES ('후라이드치킨', 16000, 2);
INSERT INTO menu (menu_name, price, store_id) VALUES ('양념치킨', 17000, 2);
INSERT INTO menu (menu_name, price, store_id) VALUES ('아메리카노', 4000, 3);