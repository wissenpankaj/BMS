// src/firebase-config.js
import { initializeApp } from "firebase/app";
import { getMessaging, onMessage } from "firebase/messaging";

const firebaseConfig = {
    apiKey: "AIzaSyCXSrXnGbj9J-I6mJHPaT3ldNa0fN2pJn8",
    authDomain: "ev-battery-fault-detection.firebaseapp.com",
    projectId: "ev-battery-fault-detection",
    storageBucket: "ev-battery-fault-detection.firebasestorage.app",
    messagingSenderId: "722372148547",
    appId: "1:722372148547:web:bc5fe499cd852d9c0883f6",
    measurementId: "G-NDL05M3D5Y"
};

// Initialize Firebase app
const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);

export { messaging, onMessage };