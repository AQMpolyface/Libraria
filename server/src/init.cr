db = DB.open "sqlite3://#{SQUEAL_LITE_DB_PATH}"

db.exec "
  CREATE TABLE IF NOT EXISTS #{DB_AUTH_TABLE} (
    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
    key TEXT UNIQUE NOT NULL,
    created_at DATETIME DEFAULT (datetime('now')),  -- current timestamp
    last_used DATETIME,                             -- nullable
    revoked INTEGER DEFAULT 0,                       -- 0 = false, 1 = true
    is_master TINYINT DEFAULT 0
);
 "

key_exists = false
db.query("SELECT key FROM #{DB_AUTH_TABLE} where is_master = 1") do |rs|
  key_exists = rs.move_next
end

base_key = random_string(KEY_LENGTH)
if key_exists
  db.query "SELECT key FROM #{DB_AUTH_TABLE} where is_master = 1 ORDER BY id LIMIT 1 " do |rs|
    rs.each do
      key = rs.read(String)
      base_key = key
    end
  end
else
  db.exec "INSERT INTO #{DB_AUTH_TABLE} (key, is_master) VALUES (?, 1) ", base_key
end

# puts base_key
puts "The master key for the server is #{base_key}"
puts "server started succesfully"
