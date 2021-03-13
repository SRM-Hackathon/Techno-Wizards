const router = require("express").Router();
const User = require("../model/User");
const verify = require("./verification");
const jwt = require("jsonwebtoken");
const uuid = require("uuid").v4;
const nodemailer = require("nodemailer");
const OtpAuth = require("../model/OtpAuth");
const crypto = require("crypto");

// Email Verification...
var otp;
var sessionid;

router.post("/sendotp", async (req, res) => {
  try {
    const emailExist = await User.findOne({ email: req.body.email });
    if (emailExist) {
      //---------Login Function---------
      //Generate OTP...
      otp = generateOtp();
      console.log(otp);

      //Initialize Nodemailer
      let transporter = setupNodeMailer();

      //Send OTP Nodemailer
      sendMail(transporter, req.body.email, otp);

      //Generate SessionID
      sessionid = generateKey();

      //Insert in Document
      var otpAuthDocument = otpDocument(req.body.email, sessionid, otp);

      try {
        //Save the OTP in Collection
        await otpAuthDocument.save();
        console.log("Message Saved...");
      } catch (err) {
        console.log(err);
      }

      res.json({ status: "OK", sessionid: sessionid }).status(200);
    } else {
      //------------ Register Function -------------
      //Generate OTP...
      otp = generateOtp();
      console.log(otp);

      //Initialize Nodemailer
      let transporter = setupNodeMailer();

      //Send OTP Nodemailer
      sendMail(transporter, req.body.email, otp);

      //Generate SessionID
      sessionid = generateKey();

      //Insert in Document
      var otpAuthDocument = otpDocument(req.body.email, sessionid, otp);

      try {
        //Save the OTP in Collection
        otpAuthDocument.save();
        console.log("Message Saved...");
      } catch (err) {
        console.log(err);
      }
      res.json({ status: "OK", sessionid: sessionid });
    }
  } catch (error) {
    console.log(error);
  }
});

router.post("/verify", async (req, res) => {
  //Get check Email already exist
  await OtpAuth.findOne(
    { sessionid: req.body.id },
    async function (err, result) {
      if (err) {
        console.log(err);
      } else {
        if (!result) {
          res.json({ message: "Database Error" }).status(502);
          return;
        }
        await User.exists({ email: result.email }, async (err, exists) => {
          if (err) {
            console.log(err);
            return;
          }
          if (exists) {
            //Redirect to Login System
            //Verify OTP
            let userOtp = req.body.otp;
            console.log(userOtp);

            //Get the USER object
            await User.findOne(
              { email: result.email },
              async (err, emailObject) => {
                if (err) {
                  console.log(err);
                } else {
                  if (userOtp === result.otp) {
                    console.log("Correct OTP");
                    console.log("This is login Function");

                    //Create and assign a token
                    const token = generateJWT(emailObject.id);
                    res.header("auth-token", token).json({ message: token });

                    //Delete the Registered OTP...
                    deleteOTP(result.sessionid, result.email);
                  } else {
                    res.json({ message: "Incorrect OTP" });
                  }
                }
              }
            );
          } else {
            //Register
            //Verify OTP
            let userOtp = req.body.otp;
            console.log(userOtp);
            if (userOtp === result.otp) {
              console.log("Correct OTP");
              console.log("This is Register Function");
              console.log(result.email);

              //Create New User
              const new_user = new User({
                id: uuid(),
                email: result.email,
              });

              //Save the User in Database
              await new_user.save();

              //Create and assign a token
              const token = generateJWT(new_user.id);
              res.header("auth-token", token).json({ message: token });

              //Delete the Registered OTP...
              deleteOTP(result.sessionid, result.email);
            } else {
              res.json({ message: "Incorrect OTP" });
            }
          }
        });
      }
    }
  );
});

//Initialize OTPAuth Model
function otpDocument(email, sessionid, otp) {
  return new OtpAuth({
    email: email,
    sessionid: sessionid,
    otp: otp,
  });
}

//Delete verified OTP
async function deleteOTP(sessionid, email) {
  await OtpAuth.findOneAndDelete(
    { sessionid: sessionid, email: email },
    (err) => {
      if (err) {
        console.log(err);
        return;
      }
      console.log("OTP Deleted...");
    }
  );
}

//Generate JWT Token
function generateJWT(userid) {
  return jwt.sign({ id: userid }, "this-is-secret-key", {
    expiresIn: "30d",
  });
}

///Generate BASE64 Key
function generateKey() {
  return crypto.randomBytes(16).toString("base64");
}

//Nodemailer Setup
function setupNodeMailer() {
  return nodemailer.createTransport({
    host: "smtp.gmail.com",
    port: 465,
    secure: true,
    service: "Gmail",
    auth: {
      user: "vishweshwaran.r@gmail.com",
      pass: "123Vishwa123",
    },
  });
}

//Generate 6-Digit OTP
function generateOtp() {
  return Math.floor(100000 + Math.random() * 900000);
}

//Send Mail using Nodemailer
async function sendMail(transporter, toAddress, otpValue) {
  return await transporter.sendMail({
    from: '"GroceGro Developers" <vishweshwaran.r@gmail.com> ',
    to: toAddress,
    subject: "Your OTP Verification Code",
    html:
      "<h3>OTP for account verification is </h3>" +
      "<h1 style='font-weight:bold;'>" +
      otpValue +
      "</h1>" +
      "<h3> Please do not share with others</h3>",
  });
}

module.exports = router;
