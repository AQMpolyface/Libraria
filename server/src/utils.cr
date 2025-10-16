def random_string(length) : String
  chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  String.build(length) do |io|
    length.times do
      io << chars.to_slice.sample
    end
  end
end
