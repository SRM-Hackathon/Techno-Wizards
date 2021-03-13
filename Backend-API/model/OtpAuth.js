const mongoose = require("mongoose");

const OtpAuthSchema = mongoose.Schema({
  email: {
    type: String,
    required: true,
  },
  sessionid: {
    type: String,
    required: true,
  },
  otp: {
    type: Number,
    required: true,
  },
  createdAt: {
    type: Date,
    default: Date.now,
  },
});

OtpAuthSchema.index({ createdAt: 1 }, { expireAfterSeconds: 60 });

module.exports = mongoose.model("otpauth", OtpAuthSchema);
