
'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotifiaction = functions.database.ref('/Notifications/{userId}/{notificationId}').onWrite((change, context) => {
  const userId = context.params.userId;
  const notificationId = context.params.notificationId;

  console.log('We have a  notification to send to : ', userId);

  if (!change.after.exists()) {
    console.log('A notification has been deleted from the database : ', notificationId);
  }

  const deviceToken = admin.database().ref('/Users/'+userId+'/deviceToken').once('value');
  return deviceToken.then(result => {
    const tokenId = result.val();
    const payload = {
      notification: {
        title : "Friend Request",
        body : "You've received a new Friend request",
        icon : "default"
      }
    };

    return admin.messaging().sendToDevice(tokenId, payload).then(response => {
      return console.log('This was the notification feature with:'+tokenId);
    });

  });

});
