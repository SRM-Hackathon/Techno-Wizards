const express = require("express");
const app = express();
const mongoose = require("mongoose");

//Routes
const authRoute = require("./routes/auth");
const storeRoute = require("./routes/store");
const userRoute = require("./routes/user");
const searchRouter = require('./routes/search');


//Connect to DB
mongoose.connect(
  "mongodb://localhost:27017/GroceGro?replicaSet=rs0",
  {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    useFindAndModify: false,
    useCreateIndex: true,
  },
  () => {
    console.log("DB connected");
  }
);

//Middlewares
app.use(express.json());

//Route Middlewares
app.use("/api/user", authRoute);
app.use("/api/store", storeRoute);
app.use("/api/users", userRoute);
app.use("/api/store", searchRouter);

var server = app.listen(5000, function () {
  console.log("Server is running on port 5000");
});
