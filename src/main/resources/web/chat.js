// function sendMessageToJava(message) {
//     console.log('Sending to Java:', message);
//     if (window.java && window.java.onMessage) {
//         window.java.onMessage(message);
//     } else {
//         console.log('Java bridge not available');
//     }
// }
//
// let userData;
// let firebaseConfig;
// let db;
// let app;
//
// // Wait for Firebase to load completely
// function initializeFirebase() {
//     console.log('Initializing Firebase...');
//
//     // Check if Firebase is loaded
//     if (typeof firebase === 'undefined') {
//         console.error('Firebase not loaded yet, retrying...');
//         setTimeout(initializeFirebase, 100);
//         return;
//     }
//
//     // Check if config is available
//     if (!window.firebaseConfig) {
//         console.log('Firebase config not available yet, retrying...');
//         setTimeout(initializeFirebase, 100);
//         return;
//     }
//
//     try {
//         userData = window.userData;
//         firebaseConfig = window.firebaseConfig;
//
//         console.log('Firebase config:', firebaseConfig);
//         console.log('User data:', userData);
//
//         // Initialize Firebase
//         app = firebase.initializeApp(firebaseConfig);
//         db = firebase.firestore();
//
//         console.log('Firebase initialized successfully');
//         document.getElementById('status').textContent = 'Firebase Ready!';
//
//         // Send success message to Java
//         sendMessageToJava('Firebase initialized successfully');
//
//     } catch (error) {
//         console.error('Firebase initialization error:', error);
//         document.getElementById('status').textContent = 'Firebase Error: ' + error.message;
//         sendMessageToJava('Firebase error: ' + error.message);
//     }
// }
//
// // Start initialization when page loads
// window.onload = function() {
//     console.log('Page loaded, starting Firebase initialization...');
//     // Give some time for Java to set up the window objects
//     setTimeout(initializeFirebase, 200);
// };
//
// // Test function to verify everything works
// function testFirestore() {
//     if (!db) {
//         console.error('Firestore not initialized');
//         return;
//     }
//
//     console.log('Testing Firestore connection...');
//     // Add your Firestore test code here
// }