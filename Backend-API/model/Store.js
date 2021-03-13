const mongoose = require("mongoose");
const mongoosastic = require("mongoosastic");

const storeSchema = mongoose.Schema({
  id: {
    type: String,
    required: true,
  },
  storeid: {
    type: String,
  },
  city: {
    type: String,
    required: true,
  },
  storename: {
    type: String,
    required: true,
    min: 8,
  },
  products: [
    {
      title: String,
      price: Number,
      qty: Number,
    },
  ],
  // post_images: [
  //   {
  //     type: String,
  //   },
  // ],
  date: {
    type: Date,
    default: Date.now,
  },
  // pincode: {
  //   type: String,
  //   required: true,
  // },
  location: {
    geo_point: {
      type: String,
      es_type: "geo_point",
    },
    lat: { type: Number },
    lon: { type: Number },
  },
});

storeSchema.plugin(mongoosastic, {
  hosts: ["localhost:9200"],
});

module.exports = mongoose.model("Store", storeSchema);
