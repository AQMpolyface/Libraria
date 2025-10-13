require "kemal"
require "sqlite3"

VERSION        = 0.1
PORT_KEY       = "LIBRARIA_PORT"
DB_KEY_PATH    = "LIBRARIA_DB_PATH"
API_KEY_HEADER = "x-auth"
DB_AUTH_TABLE  = "api_keys"

ENV[PORT_KEY] ||= "9821"
ENV[DB_KEY_PATH] ||= "./data.db"

KEY_LENGTH           = 11
PORT                 = ENV[PORT_KEY].to_i
SQUEAL_LIGHT_DB_PATH = ENV[DB_KEY_PATH]

first_boot = File.exists?(SQUEAL_LIGHT_DB_PATH)

db = DB.open "sqlite3://#{SQUEAL_LIGHT_DB_PATH}"

db.exec "
  CREATE TABLE IF NOT EXISTS #{DB_AUTH_TABLE} (
    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
    key TEXT UNIQUE,
    label TEXT,
    created_at DATETIME DEFAULT (datetime('now')),  -- current timestamp
    last_used DATETIME,                             -- nullable
    revoked INTEGER DEFAULT 0                       -- 0 = false, 1 = true
);
 "

base_key = random_string(KEY_LENGTH)
puts "The key for the server is #{base_key}"

# Auth middleware
class AuthMiddleware < Kemal::Handler
  def initialize(@db : DB::Database)
  end

  def call(context)
    key = context.response.headers[API_KEY_HEADER]
    rs = @db.exec("SELECT key FROM #{DB_AUTH_TABLE} WHERE key = ?", key)
    call_next context
  end
end

add_handler AuthMiddleware.new db

# Routes
get "/health-check" do
  "OK"
end

def random_string(length) : String
  chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  String.build(length) do |io|
    length.times do
      io << chars.to_slice.sample
    end
  end
end

Kemal.config.port = PORT
Kemal.run
