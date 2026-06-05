/**
 * MEDDY Backend API Configuration
 * 
 * This file contains all backend API endpoint configurations
 * Change BASE_URL for different environments (development, production)
 */

const API_CONFIG = {
  // Backend Base URL - CHANGE THIS FOR YOUR ENVIRONMENT
  BASE_URL: 'https://local-medicine-delivery-1.onrender.com',  // Local development
  // BASE_URL: 'https://your-production-url.com',  // Production
  
  // API Endpoints
  ENDPOINTS: {
    // ===== Authentication =====
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    
    // ===== User Management =====
    USER_PROFILE: '/api/users',                              // GET/PUT /{userId}
    USER_PASSWORD: '/api/users/{userId}/password',           // PUT
    USER_TWO_FACTOR: '/api/users/{userId}/two-factor',       // PUT
    USER_ONLINE_STATUS: '/api/users/{userId}/online-status', // PUT
    USER_ADDRESSES: '/api/users/{userId}/addresses',         // GET/POST
    DELETE_ADDRESS: '/api/users/addresses/{addressId}',      // DELETE
    
    // ===== Dashboard Statistics =====
    PATIENT_DASHBOARD: '/api/dashboard/patient/{patientId}',           // GET
    CHEMIST_DASHBOARD: '/api/dashboard/chemist/{chemistId}',           // GET
    DELIVERY_DASHBOARD: '/api/dashboard/delivery/{deliveryPartnerId}', // GET
    
    // ===== Orders =====
    CREATE_ORDER: '/api/orders/create',                               // POST
    PATIENT_ORDERS: '/api/orders/patient/{patientId}',                // GET
    CHEMIST_ORDERS: '/api/orders/chemist/{chemistId}',                // GET
    DELIVERY_ORDERS: '/api/orders/delivery/{deliveryPartnerId}',      // GET
    ORDER_BY_ID: '/api/orders/{orderId}',                             // GET
    ORDER_BY_NUMBER: '/api/orders/number/{orderNumber}',              // GET
    UPDATE_ORDER_STATUS: '/api/orders/{orderId}/status',              // PUT
    ORDERS_BY_STATUS: '/api/orders/status/{status}',                  // GET
    AVAILABLE_FOR_PICKUP: '/api/orders/available-for-pickup',         // GET
    
    // ===== Inventory/Medicines =====
    INVENTORY_ALL: '/api/inventory/all',                              // GET
    INVENTORY_SEARCH: '/api/inventory/search',                        // GET ?query=
    INVENTORY_CATEGORY: '/api/inventory/category/{category}',         // GET
    INVENTORY_ADD: '/api/inventory/add',                              // POST
    INVENTORY_UPDATE: '/api/inventory/{id}',                          // PUT
    INVENTORY_DELETE: '/api/inventory/{id}',                          // DELETE
    CHEMIST_INVENTORY: '/api/inventory/chemist/{chemistId}',          // GET
    LOW_STOCK: '/api/inventory/chemist/{chemistId}/low-stock',        // GET
    OUT_OF_STOCK: '/api/inventory/chemist/{chemistId}/out-of-stock', // GET
    MEDICINE_BY_ID: '/api/inventory/{id}',                            // GET
    
    // ===== Prescriptions =====
    PRESCRIPTION_UPLOAD: '/api/prescriptions/upload',                      // POST (multipart)
    PATIENT_PRESCRIPTIONS: '/api/prescriptions/patient/{patientId}',       // GET
    CHEMIST_PRESCRIPTIONS: '/api/prescriptions/chemist/{chemistId}',       // GET
    PRESCRIPTIONS_PENDING: '/api/prescriptions/pending',                   // GET
    PRESCRIPTION_VERIFY: '/api/prescriptions/{prescriptionId}/verify',     // PUT
    PRESCRIPTION_REJECT: '/api/prescriptions/{prescriptionId}/reject',     // PUT
    
    // ===== Notifications =====
    USER_NOTIFICATIONS: '/api/notifications/user/{userId}',                // GET
    UNREAD_NOTIFICATIONS: '/api/notifications/user/{userId}/unread',       // GET
    UNREAD_COUNT: '/api/notifications/user/{userId}/unread-count',         // GET
    MARK_READ: '/api/notifications/{notificationId}/read',                 // PUT
    MARK_ALL_READ: '/api/notifications/user/{userId}/mark-all-read',       // PUT
    
    // ===== Wishlist =====
    USER_WISHLIST: '/api/wishlist/user/{userId}',                          // GET
    WISHLIST_COUNT: '/api/wishlist/user/{userId}/count',                   // GET
    WISHLIST_ADD: '/api/wishlist/add',                                     // POST
    WISHLIST_REMOVE: '/api/wishlist/user/{userId}/item/{inventoryId}'      // DELETE
  }
};

/**
 * Helper function to replace path parameters
 * Example: replacePathParams('/api/users/{userId}', { userId: 123 })
 * Returns: '/api/users/123'
 */
function replacePathParams(path, params) {
  let result = path;
  for (const [key, value] of Object.entries(params)) {
    result = result.replace(`{${key}}`, value);
  }
  return result;
}

/**
 * Get stored JWT token from localStorage
 */
function getAuthToken() {
  return localStorage.getItem('meddyToken');
}

/**
 * Get current logged-in user from localStorage
 */
function getCurrentUser() {
  const userStr = localStorage.getItem('meddyUser');
  return userStr ? JSON.parse(userStr) : null;
}

/**
 * Save authentication data to localStorage
 */
function saveAuth(token, user) {
  localStorage.setItem('meddyToken', token);
  localStorage.setItem('meddyUser', JSON.stringify(user));
}

/**
 * Clear authentication data from localStorage
 */
function clearAuth() {
  localStorage.removeItem('meddyToken');
  localStorage.removeItem('meddyUser');
}

/**
 * Make authenticated API request
 * 
 * @param {string} endpoint - API endpoint (use API_CONFIG.ENDPOINTS)
 * @param {object} options - Fetch options (method, body, headers, etc.)
 * @param {object} pathParams - Path parameters to replace (e.g., {userId: 123})
 * @returns {Promise} - Response data or null on error
 */
async function apiRequest(endpoint, options = {}, pathParams = {}) {
  const token = getAuthToken();
  
  // Replace path parameters if provided
  let url = endpoint;
  if (Object.keys(pathParams).length > 0) {
    url = replacePathParams(endpoint, pathParams);
  }
  
  const defaultHeaders = {
    'Content-Type': 'application/json',
  };
  
  // Add Authorization header if token exists
  if (token) {
    defaultHeaders['Authorization'] = `Bearer ${token}`;
  }
  
  // Don't set Content-Type for FormData (file uploads)
  if (options.body instanceof FormData) {
    delete defaultHeaders['Content-Type'];
  }
  
  const config = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers
    }
  };
  
  try {
    const response = await fetch(API_CONFIG.BASE_URL + url, config);
    
    // Handle unauthorized (401)
    if (response.status === 401) {
      clearAuth();
      console.error('Session expired. Please login again.');
      // You can trigger a redirect to login page here
      return null;
    }
    
    // Parse JSON response
    const data = await response.json();
    
    if (!response.ok) {
      throw new Error(data.message || `Request failed with status ${response.status}`);
    }
    
    return data;
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
}

// ===== Example API Functions =====

/**
 * User Login
 */
async function login(email, password, rememberMe = false) {
  const data = await apiRequest(API_CONFIG.ENDPOINTS.LOGIN, {
    method: 'POST',
    body: JSON.stringify({ email, password, rememberMe })
  });
  
  if (data && data.token) {
    saveAuth(data.token, data.user);
    return data;
  }
  return null;
}

/**
 * User Registration
 */
async function register(userData) {
  const data = await apiRequest(API_CONFIG.ENDPOINTS.REGISTER, {
    method: 'POST',
    body: JSON.stringify(userData)
  });
  
  if (data && data.token) {
    saveAuth(data.token, data.user);
    return data;
  }
  return null;
}

/**
 * Get Patient Dashboard Statistics
 */
async function getPatientDashboard(patientId) {
  return await apiRequest(
    API_CONFIG.ENDPOINTS.PATIENT_DASHBOARD,
    { method: 'GET' },
    { patientId }
  );
}

/**
 * Get All Medicines
 */
async function getAllMedicines() {
  return await apiRequest(API_CONFIG.ENDPOINTS.INVENTORY_ALL, { method: 'GET' });
}

/**
 * Search Medicines
 */
async function searchMedicines(query) {
  return await apiRequest(
    `${API_CONFIG.ENDPOINTS.INVENTORY_SEARCH}?query=${encodeURIComponent(query)}`,
    { method: 'GET' }
  );
}

/**
 * Create Order
 */
async function createOrder(orderData) {
  return await apiRequest(API_CONFIG.ENDPOINTS.CREATE_ORDER, {
    method: 'POST',
    body: JSON.stringify(orderData)
  });
}

/**
 * Get Patient Orders
 */
async function getPatientOrders(patientId) {
  return await apiRequest(
    API_CONFIG.ENDPOINTS.PATIENT_ORDERS,
    { method: 'GET' },
    { patientId }
  );
}

/**
 * Upload Prescription
 */
async function uploadPrescription(file, patientId) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('patientId', patientId);
  
  return await apiRequest(API_CONFIG.ENDPOINTS.PRESCRIPTION_UPLOAD, {
    method: 'POST',
    body: formData
  });
}

/**
 * Get User Notifications
 */
async function getUserNotifications(userId) {
  return await apiRequest(
    API_CONFIG.ENDPOINTS.USER_NOTIFICATIONS,
    { method: 'GET' },
    { userId }
  );
}

/**
 * Get Unread Notification Count
 */
async function getUnreadCount(userId) {
  return await apiRequest(
    API_CONFIG.ENDPOINTS.UNREAD_COUNT,
    { method: 'GET' },
    { userId }
  );
}

/**
 * Add to Wishlist
 */
async function addToWishlist(userId, inventoryId) {
  return await apiRequest(API_CONFIG.ENDPOINTS.WISHLIST_ADD, {
    method: 'POST',
    body: JSON.stringify({ userId, inventoryId })
  });
}

/**
 * Logout
 */
function logout() {
  clearAuth();
  // Redirect to home page or show login modal
  console.log('User logged out');
}

// Export for use in other scripts
if (typeof module !== 'undefined' && module.exports) {
  module.exports = {
    API_CONFIG,
    apiRequest,
    login,
    register,
    logout,
    getAuthToken,
    getCurrentUser,
    saveAuth,
    clearAuth,
    replacePathParams
  };
}
