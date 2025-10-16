require "kemal"
require "sqlite3"

class AuthMiddleware < Kemal::Handler
  def initialize(@db : DB::Database)
  end

  def call(context)
    key = context.request.headers[API_KEY_HEADER]?

    unless key
      context.response.status_code = 401
      context.response.print "Missing API key"
      return
    end
    found = false
    @db.query("SELECT key FROM #{DB_AUTH_TABLE} WHERE key = ?", key) do |rs|
      found = rs.move_next
    end

    if found
      call_next context
    else
      context.response.status_code = 403
      context.response.print "Forbidden"
    end
  end
end

add_handler AuthMiddleware.new db
