-- Categories
INSERT INTO categories (name, description)
VALUES
    ('Retro Consoles', 'Classic game consoles from the 80s and 90s.'),
    ('Game Cartridges', 'Original cartridges for retro gaming systems.'),
    ('Gaming Accessories', 'Controllers, memory cards, and more.'),
    ('Collectibles', 'Figurines, posters, and rare memorabilia.'),
    ('Apparel', 'Retro-inspired fashion and wearable game merch.');

-- Retro Consoles
INSERT INTO products (name, price, category_id, description, image_url, stock, featured, color)
VALUES
    ('Super Nintendo Console', 129.99, 1, 'Super Nintendo Console - classic collectible or playable item from the retro era.', 'https://m.media-amazon.com/images/I/61WyNYECFYL._SL1500_.jpg', 100, 0, 'Gray'),
    ('SEGA Genesis Console', 119.99, 1, 'SEGA Genesis Console - classic collectible or playable item from the retro era.', 'https://m.media-amazon.com/images/I/811M8KqxeyL._SL1500_.jpg', 74, 0, 'Black'),
    ('Game Boy Color', 89.99, 1, 'Game Boy Color - classic collectible or playable item from the retro era.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/26/Nintendo_Game_Boy_Color.png/1024px-Nintendo_Game_Boy_Color.png', 31, 1, 'Purple'),
    ('NES Console', 109.99, 1, 'NES Console - classic collectible or playable item from the retro era.', 'https://m.media-amazon.com/images/I/81s7B+Als-L._SL1500_.jpg', 47, 1, 'Gray'),
    ('Atari 2600', 99.99, 1, 'Atari 2600 - classic collectible or playable item from the retro era.', 'https://m.media-amazon.com/images/I/81Gtr2INjQL._SL1500_.jpg', 18, 0, 'Black'),

-- Game Cartridges
    ('Super Mario World', 49.99, 2, 'Super Mario World - classic collectible or playable item from the retro era.', 'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcS53AJTB6CPRRzeuCnv313RIyHeT8giR6oH0MxiKrA79t8cQqsB3X7G1s8luVKA6z922VspZvW9h0wjB1K5ZiKi1tHeuZ-kMoG2Zt9hs1Gkhdeog4yswPIWzuY', 67, 1, 'Gray'),
    ('The Legend of Zelda: ALTTP', 59.99, 2, 'The Legend of Zelda: ALTTP - classic collectible or playable item from the retro era.', 'https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcSUaZtRM-6h-9XARVu4NOB8oxd0E3252qH-6PziNG02wzgsHcro5e7pTeCo6RpzclSrFP8ax_ZKVNDxR5ef5S18Gb-xIU2p05pdeWDMt5DTBmITDqDAnmOu', 65, 0, 'Gold'),
    ('Donkey Kong Country', 44.99, 2, 'Donkey Kong Country - classic collectible or playable item from the retro era.', 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRrPe2RO6XAUJ1VQkXnWnnyd-q_WWVswl52TwQwiuN1XmaSifHFc8ykNdLNJKZMiAAh6z_-fb9pKwZcecD8pp9JjBGH_bTICpPCCwW012xAnGhEIx5ZuvPp', 86, 1, 'Gray'),
    ('Metroid', 39.99, 2, 'Metroid - classic collectible or playable item from the retro era.', 'https://i.ebayimg.com/images/g/-EAAAOSwCsljHfQT/s-l1200.jpg', 40, 0, 'Gray'),
    ('F-Zero', 34.99, 2, 'F-Zero - classic collectible or playable item from the retro era.', 'https://m.media-amazon.com/images/I/51FeGpOcDkL._SY300_SX300_QL70_FMwebp_.jpg', 60, 1, 'Gray'),

-- Gaming Accessories
    ('SNES Controller', 19.99, 3, 'SNES Controller - classic collectible or playable item from the retro era.', 'https://m.media-amazon.com/images/I/71Shkjq7KjL._AC_UY218_.jpg', 65, 1, 'Gray'),
    ('Memory Card', 14.99, 3, 'Memory Card - classic collectible or playable item from the retro era.', 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcTbKfDhN7jx2eZ9fceJAZWXyAFhNMxtfpRaaCHo3OpRmI73jCaaXt612NCp4n85Cx_WakRHiL0ApMMyBpwg85SEjIp4eDpJVvbpWjlMsHC5oQF6EDupE6sU', 40, 0, 'Black'),
    ('AV Cable', 9.99, 3, 'AV Cable - classic collectible or playable item from the retro era.', 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQTayZ0dxZh7Zhx10CdclzfB_mJmc6S9Bwta_SU1dcwg5xFkKcx_8HAmRPDhGswmArFPRGlZJpY8EZ83VheYivB12RBFsflEdKtC0mBjCQqyowSS--wJyw5Tw', 54, 1, 'Black'),
    ('Power Adapter', 12.99, 3, 'Power Adapter - classic collectible or playable item from the retro era.', 'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcS8IJydhFLuOIFsZ7Mfc18b_cwkQRK77hpgvzGO1LfVxcgrEtMoMynxbwzFlXgR9-XIL1GbZOWzcZZhUuXmKt4bJIYLa1EN9eCPVPeRx8lZMrNzbx-dTq8JduCZRGiXZB4KSPgc-Mw&usqp=CAc', 34, 1, 'Black'),
    ('Game Cleaning Kit', 7.99, 3, 'Game Cleaning Kit - classic collectible or playable item from the retro era.', 'https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcTRx8JTCgi1Msq_-UjEWh7HWpShy7ELeYzNBmQBdHJFLJfzYeun0rTScKj2RF9qnvpQJl0YczPNk8-GO3bAbl5t7DjDPfsbHDfpxmsKoS8hrWjI8hJbNNu1Ptqp-Zjv4nXSHFWvjKY&usqp=CAc', 72, 0, 'White'),

-- Collectibles
    ('Mario Figurine', 24.99, 4, 'Mario Figurine - classic collectible or playable item from the retro era.', 'https://i.etsystatic.com/47671761/r/il/527b5d/6429147761/il_794xN.6429147761_imxb.jpg', 33, 0, 'Red'),
    ('Zelda Poster', 14.99, 4, 'Zelda Poster - classic collectible or playable item from the retro era.', 'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQuhfCZE-0UWidBTeOAtBGXm3Y4kRAbo_cnPFYeBgcL1hS8zlVhSmL22neggyB9CMYjrNxiyu6osISVahF3VOi0D9-nhFZbeg', 90, 1, 'Green'),
    ('Retro Game Keychain', 5.99, 4, 'Retro Game Keychain - classic collectible or playable item from the retro era.', 'https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcRQIZ43G0Nd4IzxUbLbBjqkWEZctS9ZAqumbxXYP5ieB77bIDPHMXJnWaG2TcwRBKhch3a3nzIbp4VUxO_h1afgWtdj3theqC8pJ3tH0EoH9nXk2ADQ91jCRHSfwjXXEe1pFivv0-9ISoY&usqp=CAc', 77, 1, 'Multicolor'),
    ('Yoshi Plush', 19.99, 4, 'Yoshi Plush - classic collectible or playable item from the retro era.', 'https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcQO6s61cElQgSaNeGbS4uMDLQRATEWFwb22AKt3sViZleC8cfoIws-u_ur6XFLgx4Cx67EY5vNyD5vH0b6PpPOAI4CoF7kO7YQg-lVD4fRHZdJFuv_gw5vW', 70, 0, 'Green'),
    ('SNES Mug', 11.99, 4, 'SNES Mug - classic collectible or playable item from the retro era.', 'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcTwOGg9ivqMl9GM9hYESswWDoaHr7EqTTxFwo9wXk3gxdbaGNvLrjkASe_p-a8AX6fl3bgU1zyWGmq0BL62qXkirp2O9qglfXUX6ute5abmowJIRzgtuCTh', 58, 1, 'Gray'),

-- Apparel
    ('Super Mario T-Shirt', 24.99, 5, 'Super Mario T-Shirt - classic collectible or playable item from the retro era.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRq0AIZKK8wX4oGn3iQznlNft-uRFQ0nu2x-gT6us31DOLcS2qCfrcyHsI&s', 62, 1, 'Red'),
    ('Retro Gaming Hoodie', 49.99, 5, 'Retro Gaming Hoodie - classic collectible or playable item from the retro era.', 'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcSMW4JWxr847wwAt72YPwXK6e4tpzlr0lviSJKbazk_ZdPyZpEmLKqKRxxFXUuFnLunjP1P-89-DYNLK12m6tCV1j-w333btuLtRW48UIJe65MGeWRudwDUWhE1A3_Tfg4WdM6MUA&usqp=CAc', 15, 0, 'Black'),
    ('Zelda Triforce Hat', 19.99, 5, 'Zelda Triforce Hat - classic collectible or playable item from the retro era.', 'https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcTrs0rnTbCT2FiHoq-XlYKAMUkkqhD4Zx54HpzPHsecyj4ZpFCTWd-WSDqkmJmcEveE0y-vJgaEM0Vo4i8doOb8e5WDALUBu7ffhLOR5P6KPabtEOvpyfuAXC9dX3XR-Qw-33i3-A&usqp=CAc', 95, 1, 'Gold'),
    ('Donkey Kong Socks', 9.99, 5, 'Donkey Kong Socks - classic collectible or playable item from the retro era.', 'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcQlCs52CSTnorJbZbfWshidmHRytiZ5gvjEgnH2O9zGEJxVP8LqCXRrepZMBBffc7-uC8l1wsu4goA7TKlXr9PaXoe1NrzIZO-BVVGiiVQfNEVvznnnSLhnIw', 45, 0, 'Brown'),
    ('Game Over Pajama Pants', 29.99, 5, 'Game Over Pajama Pants - classic collectible or playable item from the retro era.', 'https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcSlpGxRvJeKwb9JLLIs-th1zJoQO26eaKBfJL4FxAuc9fFv5McQeuMMQfI3ac_fs4_CTSyzgXRp61CNYioI8z9x7jfF7lBgif02Ie4-4WzzTmCRcDgiK-xh', 78, 0, 'Navy');
