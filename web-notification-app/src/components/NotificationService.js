class NotificationService {
    constructor() {
      // Store notifications in local storage to persist across refreshes
      const storedNotifications = localStorage.getItem('notifications');
      this.notifications = storedNotifications ? JSON.parse(storedNotifications) : [];
    }
  
    /**
     * Add a new notification to the list.
     * @param {string} notification - The notification message.
     */
    addNotification(notification) {
      this.notifications.push(notification);
      this.updateLocalStorage();
      this.showBrowserNotification(notification);  
    }
  
    /**
     * Retrieve all stored notifications.
     * @returns {string[]} - List of notifications.
     */
    getNotifications() {
      return this.notifications;
    }
  
    /**
     * Clear all notifications.
     */
    clearNotifications() {
      this.notifications = [];
      this.updateLocalStorage();
    }

    async showBrowserNotification(message) {
        if ('Notification' in window) {
          if (Notification.permission === 'granted') {
            new Notification('New Alert', { body: message });
          } else if (Notification.permission !== 'denied') {
            const permission = await Notification.requestPermission();
            if (permission === 'granted') {
              new Notification('New Alert', { body: message });
            } else {
              console.log('Notification permission denied.');
            }
          } else {
            console.log('Notifications are disabled.');
          }
        } else {
          console.log('Browser does not support notifications.');
        }
    }
  
    /**
     * Update the local storage with the current notifications.
     */
    updateLocalStorage() {
      localStorage.setItem('notifications', JSON.stringify(this.notifications));
    }
  }

export default NotificationService;  