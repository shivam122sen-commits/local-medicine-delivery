const API_BASE = "https://local-medicine-delivery-1.onrender.com";
let currentUser = null;

// How JavaScript Fetches Data:
// We use the async/await paradigm with the native fetch() function.
// We pass JSON strings inside the request body and capture JSON responses from Spring Boot.

async function handleAuth(action) {
    const name = document.getElementById("auth-name").value;
    const email = document.getElementById("auth-email").value;
    const password = document.getElementById("auth-password").value;
    const role = document.getElementById("auth-role").value;

    const endpoint = action === 'login' ? '/auth/login' : '/auth/register';
    const bodyData = action === 'login' ? { email, password } : { name, email, password, role };

    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bodyData)
        });

        if (!response.ok) throw new Error(await response.text());
        
        const data = await response.json();
        currentUser = data;
        alert(`${action.toUpperCase()} Successful!`);
        setupDashboard();
    } catch (err) {
        alert("Error: " + err.message);
    }
}

function setupDashboard() {
    document.getElementById("auth-section").style.display = "none";
    document.getElementById("logout-btn").style.display = "block";
    document.getElementById("user-info").innerText = `Logged in as: ${currentUser.name} (${currentUser.role})`;

    if (currentUser.role === "PATIENT") {
        document.getElementById("patient-dashboard").style.display = "block";
        loadPatientOrders();
    } else if (currentUser.role === "CHEMIST") {
        document.getElementById("chemist-dashboard").style.display = "block";
        loadChemistOrders();
    } else if (currentUser.role === "DELIVERY") {
        document.getElementById("delivery-dashboard").style.display = "block";
        loadDeliveryOrders();
    }
}

// ================= PATIENT FEATURES =================
async function createOrder() {
    const url = document.getElementById("prescription-url").value;
    const chemistId = document.getElementById("order-chemist-id").value;

    const response = await fetch(`${API_BASE}/orders/create`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ patientId: currentUser.id, chemistId, prescriptionUrl: url })
    });
    if(response.ok) { alert("Order placed!"); loadPatientOrders(); }
}

async function loadPatientOrders() {
    const res = await fetch(`${API_BASE}/orders/patient/${currentUser.id}`);
    const orders = await res.json();
    const list = document.getElementById("patient-orders-list");
    list.innerHTML = orders.map(o => `<li class="order-item">Order #${o.id} - Status: <strong>${o.status}</strong></li>`).join('');
}

// ================= CHEMIST FEATURES =================
async function addInventory() {
    const medicineName = document.getElementById("med-name").value;
    const price = document.getElementById("med-price").value;
    const stock = document.getElementById("med-stock").value;

    await fetch(`${API_BASE}/inventory/add`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ chemistId: currentUser.id, medicineName, price, stock })
    });
    alert("Medicine inventory updated!");
}

async function loadChemistOrders() {
    const res = await fetch(`${API_BASE}/orders/chemist/${currentUser.id}`);
    const orders = await res.json();
    const container = document.getElementById("chemist-orders-container");
    container.innerHTML = orders.map(o => `
        <div class="order-item">
            <p>Order #${o.id} - Prescription: ${o.prescriptionUrl} [Status: ${o.status}]</p>
            <button onclick="updateOrderStatus(${o.id}, 'APPROVED')">Approve</button>
            <button style="background:red;" onclick="updateOrderStatus(${o.id}, 'REJECTED')">Reject</button>
        </div>
    `).join('');
}

// ================= DELIVERY PARTNER FEATURES =================
async function loadDeliveryOrders() {
    const res = await fetch(`${API_BASE}/orders/ready-for-pickup`);
    const orders = await res.json();
    const container = document.getElementById("delivery-orders-container");
    container.innerHTML = orders.map(o => `
        <div class="order-item">
            <p>Order #${o.id} is ready for delivery.</p>
            <button onclick="updateOrderStatus(${o.id}, 'DELIVERED')">Mark as Delivered</button>
        </div>
    `).join('');
}

async function updateOrderStatus(orderId, status) {
    const devIdParam = status === 'DELIVERED' ? `&deliveryPartnerId=${currentUser.id}` : '';
    const res = await fetch(`${API_BASE}/orders/update-status/${orderId}?status=${status}${devIdParam}`, {
        method: 'PUT'
    });
    if(res.ok) {
        alert("Status updated!");
        setupDashboard();
    }
}

function logout() { location.reload(); }