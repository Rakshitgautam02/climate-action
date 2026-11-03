// ========================================
// CLIMATE ACTION - FRONTEND JAVASCRIPT
// ========================================

const API_BASE_URL = `http://${window.location.hostname}:8080`;

let footprintChart = null;


// ========================================
// REGISTER
// ========================================

const registerForm = document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const name = document.getElementById("registerName").value.trim();
        const email = document.getElementById("registerEmail").value.trim();
        const password = document.getElementById("registerPassword").value;

        try {

            const response = await fetch(`${API_BASE_URL}/api/auth/register`, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    name: name,
                    email: email,
                    password: password
                })

            });

            const data = await response.json();

            if (!response.ok) {

                throw new Error(
                    data.message || data.error || "Registration failed"
                );

            }

            alert("Account created successfully! Please log in.");

            localStorage.removeItem("jwtToken");
            localStorage.removeItem("userEmail");

            window.location.href = "login.html";

        } catch (error) {

            alert(error.message);

        }

    });

}


// ========================================
// LOGIN
// ========================================

const loginForm = document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value;

        try {

            const response = await fetch(`${API_BASE_URL}/api/auth/login`, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    email: email,
                    password: password
                })

            });

            const data = await response.json();

            if (!response.ok) {

                throw new Error(
                    data.message || data.error || "Login failed"
                );

            }

            if (!data.token) {
                throw new Error("Login response did not contain a token.");
            }

            localStorage.setItem("jwtToken", data.token);
            localStorage.setItem("userEmail", email);

            window.location.href = "dashboard.html";

        } catch (error) {

            alert(error.message);

        }

    });

}


// ========================================
// DASHBOARD AUTHENTICATION
// ========================================

const dashboardSection = document.getElementById("dashboardSection");

if (dashboardSection) {

    const token = localStorage.getItem("jwtToken");

    if (!token) {

        window.location.replace("login.html");

    } else {

        initializeDashboard();

    }

}


async function initializeDashboard() {

    document.getElementById("welcomeMessage").textContent =
        "Welcome, " + localStorage.getItem("userEmail");

    await loadFootprintHistory();

}


// ========================================
// CARBON FOOTPRINT CALCULATOR
// ========================================

const calculatorForm = document.getElementById("calculatorForm");

if (calculatorForm) {

    calculatorForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const token = localStorage.getItem("jwtToken");

        if (!token) {

            alert("Please log in first.");

            window.location.href = "login.html";

            return;

        }

        const requestData = {

            electricity: Number(document.getElementById("electricity").value),

            carDistance: Number(document.getElementById("carDistance").value),

            motorcycleDistance: Number(document.getElementById("motorcycleDistance").value),

            busDistance: Number(document.getElementById("busDistance").value)

        };

        try {

            const response = await fetch(`${API_BASE_URL}/api/footprints`, {

                method: "POST",

                headers: {

                    "Content-Type": "application/json",

                    "Authorization": `Bearer ${token}`

                },

                body: JSON.stringify(requestData)

            });

            const data = await response.json();

            if (!response.ok) {

                throw new Error(
                    data.message || data.error || "Calculation failed"
                );

            }

            document.getElementById("results").hidden = false;

            document.getElementById("totalEmissions").textContent =
                Number(data.totalEmissions).toFixed(2) + " kg CO₂e";

            document.getElementById("electricityResult").textContent =
                Number(data.electricityEmissions).toFixed(2) + " kg CO₂e";

            document.getElementById("carResult").textContent =
                Number(data.carEmissions).toFixed(2) + " kg CO₂e";

            document.getElementById("motorcycleResult").textContent =
                Number(data.motorcycleEmissions).toFixed(2) + " kg CO₂e";

            document.getElementById("busResult").textContent =
                Number(data.busEmissions).toFixed(2) + " kg CO₂e";

            alert("Your footprint has been saved successfully!");

            await loadFootprintHistory();

        } catch (error) {

            alert(error.message);

        }

    });

}


// ========================================
// LOAD FOOTPRINT HISTORY
// ========================================

async function loadFootprintHistory() {

    const token = localStorage.getItem("jwtToken");

    if (!token) {
        return;
    }

    try {

        const response = await fetch(`${API_BASE_URL}/api/footprints`, {

            method: "GET",

            headers: {
                "Authorization": `Bearer ${token}`
            }

        });

        if (!response.ok) {

            if (response.status === 401 || response.status === 403) {

                logoutUser();

                alert("Your session has expired. Please log in again.");

                return;

            }

            throw new Error("Unable to load footprint history");

        }

        const records = await response.json();

        displayFootprintChart(records);

    } catch (error) {

        console.error("History error:", error);

    }

}


// ========================================
// DISPLAY DASHBOARD AND GRAPH
// ========================================

function displayFootprintChart(records) {

    if (!Array.isArray(records)) {
        return;
    }

    if (records.length === 0) {

        document.getElementById("dashboardTotal").textContent = "0.00";

        document.getElementById("dashboardElectricity").textContent = "0.00";

        document.getElementById("dashboardTransport").textContent = "0.00";

        document.getElementById("dashboardRecords").textContent = "0";

        if (footprintChart) {

            footprintChart.destroy();

            footprintChart = null;

        }

        return;
    }

    const sortedRecords = [...records].sort((a, b) => {

        return new Date(a.weekStart) - new Date(b.weekStart);

    });

    const latestRecord = sortedRecords[sortedRecords.length - 1];

    const electricity = Number(latestRecord.electricityEmissions || 0);

    const car = Number(latestRecord.carEmissions || 0);

    const motorcycle = Number(latestRecord.motorcycleEmissions || 0);

    const bus = Number(latestRecord.busEmissions || 0);

    const transport = car + motorcycle + bus;

    document.getElementById("dashboardTotal").textContent =
        Number(latestRecord.totalEmissions || 0).toFixed(2);

    document.getElementById("dashboardElectricity").textContent =
        electricity.toFixed(2);

    document.getElementById("dashboardTransport").textContent =
        transport.toFixed(2);

    document.getElementById("dashboardRecords").textContent =
        records.length;

    const labels = sortedRecords.map(record => {

        return "Week of " + record.weekStart;

    });

    const electricityData = sortedRecords.map(record => {

        return Number(record.electricityEmissions || 0);

    });

    const transportData = sortedRecords.map(record => {

        return (
            Number(record.carEmissions || 0) +
            Number(record.motorcycleEmissions || 0) +
            Number(record.busEmissions || 0)
        );

    });

    const canvas = document.getElementById("footprintChart");

    if (!canvas || typeof Chart === "undefined") {
        return;
    }

    const context = canvas.getContext("2d");

    if (footprintChart) {

        footprintChart.destroy();

    }

    footprintChart = new Chart(context, {

        type: "bar",

        data: {

            labels: labels,

            datasets: [

                {
                    label: "Electricity Emissions",
                    data: electricityData,
                    backgroundColor: "#39875b",
                    borderRadius: 5
                },

                {
                    label: "Transport Emissions",
                    data: transportData,
                    backgroundColor: "#6542a6",
                    borderRadius: 5
                }

            ]

        },

        options: {

            responsive: true,

            maintainAspectRatio: true,

            plugins: {

                title: {
                    display: true,
                    text: "Your Weekly Carbon Footprint"
                },

                legend: {
                    position: "bottom"
                }

            },

            scales: {

                x: {
                    stacked: true
                },

                y: {

                    stacked: true,

                    beginAtZero: true,

                    title: {
                        display: true,
                        text: "Emissions (kg CO₂e)"
                    }

                }

            }

        }

    });

}


// ========================================
// REFRESH HISTORY
// ========================================

const refreshHistoryButton = document.getElementById("refreshHistoryButton");

if (refreshHistoryButton) {

    refreshHistoryButton.addEventListener("click", function () {

        loadFootprintHistory();

    });

}


// ========================================
// LOGOUT
// ========================================

function logoutUser() {

    localStorage.removeItem("jwtToken");

    localStorage.removeItem("userEmail");

    if (footprintChart) {

        footprintChart.destroy();

        footprintChart = null;

    }

    window.location.replace("index.html");

}


const logoutButton = document.getElementById("logoutButton");

if (logoutButton) {

    logoutButton.addEventListener("click", function () {

        logoutUser();

    });

}