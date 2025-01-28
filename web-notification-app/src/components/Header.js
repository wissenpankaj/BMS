import React, { useState, useEffect, useRef } from 'react';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';
import { initializeApp } from 'firebase/app';
import NotificationService from './NotificationService'; // Import NotificationService
import { firebaseConfig, vapidKey } from '../config/firebaseConfig'; // Import your Firebase config here
import 'bootstrap/dist/css/bootstrap.min.css';
import { Modal } from 'react-bootstrap'; // React Bootstrap modal for notifications
import "./Header.css";

function Header() {
  const [notifications, setNotifications] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [, setPermissionGranted] = useState(false);
  const messaging = useRef(null); // To store the messaging instance
  const notificationService = new NotificationService();

  useEffect(() => {
    const storedNotifications = notificationService.getNotifications();
    setNotifications(storedNotifications);
  }, []);

  useEffect(() => {
    // Initialize Firebase
    const app = initializeApp(firebaseConfig);
    messaging.current = getMessaging(app);

    // Listen for foreground notifications
    onMessage(messaging.current, (payload) => {
      console.log('Message received. ', payload);
      const notification = payload.notification?.body;
      if (notification) {
        notificationService.addNotification(notification);
        setNotifications(notificationService.getNotifications()); // Update state with new notifications
      }
    });

    // Request token on mount
    getToken(messaging.current, {
      vapidKey: vapidKey, // Use the imported vapidKey
    }).then((token) => {
      if (token) {
        console.log('FCM Token:', token);
      } else {
        console.log('No FCM token available');
      }
    }).catch((error) => {
      console.error('Error getting FCM token:', error);
    });

    // Check if browser supports notifications and request permission
    if (Notification.permission === 'granted') {
      setPermissionGranted(true);
    } else if (Notification.permission === 'denied') {
      setPermissionGranted(false);
    }
  }, []);

  const toggleNotifications = () => {
    setShowModal(!showModal); // Toggle modal visibility state
  };

  const addSampleNotification = () => {
    const sampleNotification = `Sample Notification at ${new Date().toLocaleTimeString()}`;
    notificationService.addNotification(sampleNotification);
    setNotifications(notificationService.getNotifications()); // Update state to show the new notification
  };

  return (
    <div className="navbar navbar-expand-lg navbar-light bg-light shadow-sm me-3">
      <div className="container-fluid">
        <a className="navbar-brand" href="#">Notification App</a>
        <div className="d-flex align-items-center">
          {/* Notification Bell */}
          <div className="dropdown">
            <button
              className="btn btn-light position-relative me-3"
              type="button"
              onClick={toggleNotifications} // Open the modal on click
            >
              <i className="fa fa-bell fa-lg"></i>
              <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                {notifications.length}
              </span>
            </button>
          </div>

          {/* Add New Notification Button */}
          <button
            className="btn btn-outline-success ms-2"
            onClick={addSampleNotification} // Add a sample notification on click
          >
            Add New Notification
          </button>
        </div>
      </div>

      {/* Bootstrap Modal for notification Popup */}
      <Modal show={showModal} onHide={toggleNotifications}>
        <Modal.Header closeButton>
          <Modal.Title>Notifications</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {/* Loop through notifications and display them */}
          {notifications.length === 0 ? (
            <p>No notifications</p>
          ) : (
            <ul className="list-group">
              {notifications.map((notification, index) => (
                <li key={index} className="list-group-item">
                  {notification}
                </li>
              ))}
            </ul>
          )}
        </Modal.Body>
        <Modal.Footer>
          <button className="btn btn-secondary" onClick={toggleNotifications}>
            Close
          </button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default Header;