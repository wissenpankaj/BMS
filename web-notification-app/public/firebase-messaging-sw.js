// public/firebase-messaging-sw.js
importScripts("https://www.gstatic.com/firebasejs/11.2.0/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/11.2.0/firebase-messaging-compat.js");

firebase.initializeApp({
    apiKey: "AIzaSyCXSrXnGbj9J-I6mJHPaT3ldNa0fN2pJn8",
    authDomain: "ev-battery-fault-detection.firebaseapp.com",
    projectId: "ev-battery-fault-detection",
    storageBucket: "ev-battery-fault-detection.firebasestorage.app",
    messagingSenderId: "722372148547",
    appId: "1:722372148547:web:bc5fe499cd852d9c0883f6",
    measurementId: "G-NDL05M3D5Y"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log("Background message received: ", payload);
  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
    icon: payload.notification.icon,
  };
  self.registration.showNotification(notificationTitle, notificationOptions);
});