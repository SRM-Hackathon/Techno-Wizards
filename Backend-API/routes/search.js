const router = require('express').Router();
const Store = require('../model/Store')
const verify = require('./verification');


var dataArray = []


Store.createMapping(function (err, mapping) {
    if (err) {
        console.log("error creaing mapping");
        console.log(err);
    } else {
        console.log("mapping created");
        console.log(mapping);
    }
});

var stream = Store.synchronize();
var count = 0;

stream.on('data', function () {
    count++;
});

stream.on('close', function () {
    console.log("Indexed" + count + "documents");
});

stream.on('error', function (err) {
    console.log(err)
});

router.get('/search', verify, function (req, res, next) {

    if (req.query.q) {
        Store.search({
            function_score: {
                query: {
                    multi_match: {
                        query: req.query.q,
                        fuzziness: "AUTO",
                        prefix_length: 2
                    }
                }
            }
        }, function (err, results) {

            if (err) {
                console.log(`This is the error ${err}`)
                return next(err)
            }

            var data = results.hits.hits.map(function (hit) {
                return hit;
            });

            var filteredData = data.forEach(function (items) {
                console.log(items);
                dataArray.push(items._source)
            })

            if (dataArray.length !== 0) {
                res.json(dataArray)
                dataArray = []
            }
            else {
                res.status(200).json({ message: "No Items found" })
            }
        });
    }

});

module.exports = router