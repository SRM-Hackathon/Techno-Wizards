const router = require("express").Router();
const verify = require("./verification");
const Store = require("../model/Store");
const uuid = require("uuid").v4;

router.post("/newstore", verify, async (req, res) => {
  var storeID = getUUID();

  try {
    const storeData = {
      storeid: storeID,
      id: req.user.id,
      city: req.body.city,
      storename: req.body.storename,
      location: {
        lat: req.body.lat,
        lon: req.body.lon
    }
    };

    const newStore = new Store(storeData);

    await newStore.save(function (err, store) {
      if (err) {
        console.log(err);
        res.json({ message: "Error" }).status(500);
      } else {
        store.save(function (err) {
          if (err) throw err;
          store.on('es-indexed', function (err, res) {
              if (err) throw err;
              console.log("Data indexed to Elastic Search " + JSON.stringify(res))
          });

      });
        console.log(store);
        //Response...
        res.status(201).json({
          message: "Store added Successfully",
          storeid: `${storeData.storeid}`,
        });
      }
    });
  } catch (e) {
    console.log(e);
    res.json({ message: "Error Occured" }).status(500);
  }
});

//Add products to the store
router.post("/addproducts", verify, async (req, res) => {
  const checkStoreExist = await Store.findOne({
    id: req.user.id,
    storeid: req.query.storeid,
  });
  if (checkStoreExist === null) {
    res.json({ message: "Store not found" }).status(404);
    return;
  }

  //Get items data
  const productsData = req.body.products;

  const document = {
    products: productsData,
  };

  console.log(document);
  try {
    await Store.findOneAndUpdate(
      { id: req.user.id, storeid: req.query.storeid },
      document,
      { upsert: true, new: true }
    );
    res.status(200).json({ message: "Products updated" });
  } catch (e) {
    console.log(e);
  }
});

//TODO:- Make isAdmin to true if he has a store

function getUUID() {
  return uuid();
}

module.exports = router;
