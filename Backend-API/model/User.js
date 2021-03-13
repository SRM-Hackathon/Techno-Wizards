const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
  id: {
    type: String,
  },
  full_name: {
    type: String,
  },
  email: {
    type: String,
  },
  date: {
    type: Date,
    required: true,
    default: Date.now,
  },
  isAdmin: {
    type: Boolean,
    default: false,
  },
});

module.exports = mongoose.model("User", userSchema);
