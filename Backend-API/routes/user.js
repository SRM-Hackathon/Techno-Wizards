const router = require("express").Router();
const verify = require("./verification");
const Store = require("../model/Store");
const Orders = require("../model/Orders");

var items1;

router.post("/buyproducts", verify, (req, res) => {
  var storeId = req.body.storeid;
  var products = req.body.products;
  //Check Store exists or not...
  Store.exists({ storeid: storeId }, async (err, result) => {
    if (err) {
      console.log(err);
    } else {
      if (result) {
        var orderDetails = {
          id: req.user.id,
          storeid: storeId,
          products: products,
        };
        const newOrder = new Orders(orderDetails);

        await newOrder.save((err, order) => {
          if (order) {
            res.json({ message: "Order request sent" }).status(200);
          } else {
            res.json({ message: "Store not found" }).status(404);
          }
        });
      }
    }
  });
});

router.get("/getallhisorder", verify, async (req, res) => {
  await Orders.find({ storeid: req.body.storeid }, (err, result) => {
    if (!result) {
      res.json({ message: "No Orders found" }).status(200);
      return;
    }
    res.json({ message: result });
  });
});

router.post("/orderaccept", verify, async (req, res) => {
  const storeID = req.body.storeid;
  console.log(storeID);
  await Orders.find({ storeid: storeID }, async (err, result) => {
    if (!result) {
      res.json({ message: "No Orders found" }).status(200);
    } else {
      await Store.find({ storeid: storeID }, async (err, storeResult) => {
        if (err) {
          console.log(err);
          return;
        } else {
          console.log("Before");
          if (storeResult) {
            var storeProducts = storeResult[0].products;
            var orderedProducts = result[0].products;
            storeProducts.forEach(function (item, index) {
              items1 = item.title;
              orderedProducts.forEach(function (item2, index2) {
                if (items1 === item2.title) {
                  var newQty = item.qty - item2.qty;
                  storeProducts[index].qty = newQty;
                }
              });
            });
          }
          var documentUpdate = {
            products: storeProducts,
          };

          await Store.findOneAndUpdate(
            { id: req.user.id, storeid: storeID },
            documentUpdate,
            { upsert: true, new: true }
          );
          res.json({ message: "Order Placed" });
        }
      });
    }
  });
});

module.exports = router;
