
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


  const fromUser = admin.database().ref('/Notifications/' + userId + '/' + notificationId).once('value');
  return fromUser.then(fromUserResult => {

    const fromUserId = fromUserResult.val().from;
    console.log('You have new notification from : ', fromUserId);

    const userQuery = admin.database().ref('/Users/' + fromUserId + '/email').once('value');
    const deviceToken = admin.database().ref('/Users/'+ userId +'/deviceToken').once('value');

    return Promise.all([userQuery, deviceToken]).then(result => {
      const userEmail = result[0].val();
      const tokenId = result[1].val();

      const payload = {
        notification: {
          title : "Friend Request",
          body : userEmail + " has sent you Friend request",
          icon : "default",
          clickAction : "com.example.lai.toolsman_TARGET_NOTIFICATION"

        },
        data : {
          fromUserId : fromUserId
        }
      };

      return admin.messaging().sendToDevice(tokenId, payload).then(response => {
        return console.log('This was the notification feature with:'+ tokenId);
      });

    });

  });

});
