def random_string(len : Int) : String
  chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  String.new(Bytes.new(chars.to_slice.sample(len).to_unsafe, len))
end
