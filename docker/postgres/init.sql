-- RESET
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS category CASCADE;

-- SCHEMA
CREATE TABLE category (
  id          BIGSERIAL PRIMARY KEY,
  code        VARCHAR(32) UNIQUE NOT NULL,
  name        VARCHAR(128) NOT NULL,
  updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE item (
  id          BIGSERIAL PRIMARY KEY,
  sku         VARCHAR(64) UNIQUE NOT NULL,
  name        VARCHAR(128) NOT NULL,
  price       NUMERIC(10,2) NOT NULL,
  stock       INT NOT NULL,
  category_id BIGINT NOT NULL REFERENCES category(id),
  updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_item_category   ON item(category_id);
CREATE INDEX idx_item_updated_at ON item(updated_at);

-- 2000 CATEGORIES CAT0001..CAT2000
INSERT INTO category(code, name)
SELECT FORMAT('CAT%04s', g) AS code,
       FORMAT('Category %s', g) AS name
FROM generate_series(1,2000) g;

-- 100 000 ITEMS (~50 / catégorie)
WITH params AS (
  SELECT c.id AS cat_id, c.code FROM category c
),
gen AS (
  SELECT
    p.cat_id,
    i AS idx,
    FORMAT('%s-SKU-%05s', (SELECT code FROM category WHERE id=p.cat_id), i) AS sku,
    FORMAT('Item %s-%05s', p.cat_id, i) AS name,
    round( (random()*95 + 5)::numeric, 2 ) AS price,   -- 5..100
    (random()*200)::int AS stock                       -- 0..200
  FROM params p, generate_series(1,50) i
)
INSERT INTO item (sku,name,price,stock,category_id)
SELECT sku, name, price, stock, cat_id
FROM gen;

ANALYZE;
