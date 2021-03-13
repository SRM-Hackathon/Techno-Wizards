const mongoose = require("mongoose");

const OrdersSchema = mongoose.Schema({
  id: {
    type: String,
  },
  storeid: {
    type: String,
  },
  products: [
    {
      title: String,
      qty: Number,
    },
  ],
});

module.exports = mongoose.model("orders", OrdersSchema);
