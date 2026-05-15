CREATE TABLE IF NOT EXISTS room_users( 
   id  SERIAL NOT NULL,
  user_id INT NOT NULL,
  room_id INT NOT NULL,
  PRIMARY KEY (id),
  -- 外部のキーを参照するにはFOREIGN文を用いる
  -- ON DELETE CASCADEは、関連するテーブルの値が削除された際にこのテーブルのレコードも一緒に削除するための設定
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);