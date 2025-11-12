const userService = new UserService();
const familyService = new FamilyService();

let currentEditingUserId = null;
let currentEditingFamilyId = null;

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    loadDashboardStats();
    loadUsers();
    loadUserFilter();
    loadEmergencyContacts();
});

// Navigation
function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Remove active class from all nav links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    // Show selected section
    document.getElementById(sectionId).classList.add('active');
    
    // Add active class to corresponding nav link
    document.querySelector(`[href="#${sectionId}"]`).classList.add('active');
    
    // Load section-specific data
    if (sectionId === 'users') {
        loadUsers();
    } else if (sectionId === 'family') {
        loadFamilyMembers();
    } else if (sectionId === 'emergency') {
        loadEmergencyContacts();
    }
}

// Dashboard functions
async function loadDashboardStats() {
    const usersResponse = await userService.getAllUsers();
    if (usersResponse.status === 'SUCCESS') {
        document.getElementById('totalUsers').textContent = usersResponse.data.length;
        
        // Calculate total family members
        let totalFamily = 0;
        for (const user of usersResponse.data) {
            const familyResponse = await familyService.getFamilyMembersByUserId(user.id);
            if (familyResponse.status === 'SUCCESS') {
                totalFamily += familyResponse.data.length;
            }
        }
        document.getElementById('totalFamily').textContent = totalFamily;
        
        // Calculate emergency contacts
        let emergencyContacts = 0;
        for (const user of usersResponse.data) {
            const emergencyResponse = await familyService.getEmergencyContacts(user.id);
            if (emergencyResponse.status === 'SUCCESS') {
                emergencyContacts += emergencyResponse.data.length;
            }
        }
        document.getElementById('emergencyContacts').textContent = emergencyContacts;
    }
}

// User Management
async function loadUsers() {
    const response = await userService.getAllUsers();
    const tbody = document.getElementById('usersTableBody');
    
    if (response.status === 'SUCCESS') {
        tbody.innerHTML = response.data.map(user => `
            <tr>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td>${user.bloodGroup || 'N/A'}</td>
                <td>
                    <button class="btn-action btn-edit" onclick="editUser(${user.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-action btn-delete" onclick="deleteUser(${user.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } else {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">No users found</td></tr>';
    }
}

function showUserForm() {
    document.getElementById('userForm').style.display = 'block';
    document.getElementById('userFormTitle').textContent = 'Add New User';
    document.getElementById('userFormElement').reset();
    currentEditingUserId = null;
}

function hideUserForm() {
    document.getElementById('userForm').style.display = 'none';
}

async function handleUserSubmit(event) {
    event.preventDefault();
    
    const formData = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        password: document.getElementById('password').value,
        dateOfBirth: document.getElementById('dateOfBirth').value,
        address: document.getElementById('address').value,
        bloodGroup: document.getElementById('bloodGroup').value
    };
    
    let response;
    if (currentEditingUserId) {
        response = await userService.updateUser(currentEditingUserId, formData);
    } else {
        response = await userService.createUser(formData);
    }
    
    if (response.status === 'SUCCESS') {
        showToast('User saved successfully!', 'success');
        hideUserForm();
        loadUsers();
        loadDashboardStats();
        loadUserFilter();
    } else {
        showToast(response.message, 'error');
    }
}

async function editUser(userId) {
    const response = await userService.getUserById(userId);
    if (response.status === 'SUCCESS') {
        const user = response.data;
        document.getElementById('userId').value = user.id;
        document.getElementById('name').value = user.name;
        document.getElementById('email').value = user.email;
        document.getElementById('phone').value = user.phone;
        document.getElementById('dateOfBirth').value = user.dateOfBirth || '';
        document.getElementById('address').value = user.address || '';
        document.getElementById('bloodGroup').value = user.bloodGroup || '';
        document.getElementById('password').required = false;
        
        document.getElementById('userFormTitle').textContent = 'Edit User';
        document.getElementById('userForm').style.display = 'block';
        currentEditingUserId = userId;
    }
}

async function deleteUser(userId) {
    if (confirm('Are you sure you want to delete this user?')) {
        const response = await userService.deleteUser(userId);
        if (response.status === 'SUCCESS') {
            showToast('User deleted successfully!', 'success');
            loadUsers();
            loadDashboardStats();
            loadUserFilter();
        } else {
            showToast(response.message, 'error');
        }
    }
}

async function searchUsers() {
    const searchTerm = document.getElementById('userSearch').value;
    if (searchTerm.length > 2) {
        const response = await userService.searchUsers(searchTerm);
        const tbody = document.getElementById('usersTableBody');
        
        if (response.status === 'SUCCESS') {
            tbody.innerHTML = response.data.map(user => `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.phone}</td>
                    <td>${user.bloodGroup || 'N/A'}</td>
                    <td>
                        <button class="btn-action btn-edit" onclick="editUser(${user.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" onclick="deleteUser(${user.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `).join('');
        }
    } else if (searchTerm.length === 0) {
        loadUsers();
    }
}

// Family Management
async function loadUserFilter() {
    const response = await userService.getAllUsers();
    const select = document.getElementById('userFilter');
    const familyUserSelect = document.getElementById('familyUserId');
    
    if (response.status === 'SUCCESS') {
        const options = response.data.map(user => 
            `<option value="${user.id}">${user.name} (${user.phone})</option>`
        ).join('');
        
        select.innerHTML = '<option value="">All Users</option>' + options;
        familyUserSelect.innerHTML = '<option value="">Select User</option>' + options;
    }
}

async function loadFamilyMembers() {
    const userId = document.getElementById('userFilter').value;
    let response;
    
    if (userId) {
        response = await familyService.getFamilyMembersByUserId(userId);
    } else {
        // Load all family members (you might need to implement this endpoint)
        response = { status: 'SUCCESS', data: [] };
        // For demo, we'll just show empty
    }
    
    const tbody = document.getElementById('familyTableBody');
    
    if (response.status === 'SUCCESS') {
        tbody.innerHTML = response.data.map(member => `
            <tr>
                <td>${member.userId}</td>
                <td>${member.name}</td>
                <td>${member.relationship}</td>
                <td>${member.phone}</td>
                <td>${member.isEmergencyContact ? '<span class="badge danger">Yes</span>' : 'No'}</td>
                <td>
                    <button class="btn-action btn-edit" onclick="editFamilyMember(${member.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn-action btn-delete" onclick="deleteFamilyMember(${member.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } else {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No family members found</td></tr>';
    }
}

function showFamilyForm() {
    document.getElementById('familyForm').style.display = 'block';
    document.getElementById('familyFormTitle').textContent = 'Add Family Member';
    document.getElementById('familyFormElement').reset();
    currentEditingFamilyId = null;
}

function hideFamilyForm() {
    document.getElementById('familyForm').style.display = 'none';
}

async function handleFamilySubmit(event) {
    event.preventDefault();
    
    const formData = {
        userId: document.getElementById('familyUserId').value,
        name: document.getElementById('familyName').value,
        relationship: document.getElementById('relationship').value,
        phone: document.getElementById('familyPhone').value,
        email: document.getElementById('familyEmail').value,
        isEmergencyContact: document.getElementById('isEmergencyContact').checked
    };
    
    let response;
    if (currentEditingFamilyId) {
        response = await familyService.updateFamilyMember(currentEditingFamilyId, formData);
    } else {
        response = await familyService.createFamilyMember(formData);
    }
    
    if (response.status === 'SUCCESS') {
        showToast('Family member saved successfully!', 'success');
        hideFamilyForm();
        loadFamilyMembers();
        loadDashboardStats();
        loadEmergencyContacts();
    } else {
        showToast(response.message, 'error');
    }
}

async function editFamilyMember(familyId) {
    const response = await familyService.getFamilyMemberById(familyId);
    if (response.status === 'SUCCESS') {
        const member = response.data;
        document.getElementById('familyId').value = member.id;
        document.getElementById('familyUserId').value = member.userId;
        document.getElementById('familyName').value = member.name;
        document.getElementById('relationship').value = member.relationship;
        document.getElementById('familyPhone').value = member.phone;
        document.getElementById('familyEmail').value = member.email || '';
        document.getElementById('isEmergencyContact').checked = member.isEmergencyContact;
        
        document.getElementById('familyFormTitle').textContent = 'Edit Family Member';
        document.getElementById('familyForm').style.display = 'block';
        currentEditingFamilyId = familyId;
    }
}

async function deleteFamilyMember(familyId) {
    if (confirm('Are you sure you want to delete this family member?')) {
        const response = await familyService.deleteFamilyMember(familyId);
        if (response.status === 'SUCCESS') {
            showToast('Family member deleted successfully!', 'success');
            loadFamilyMembers();
            loadDashboardStats();
            loadEmergencyContacts();
        } else {
            showToast(response.message, 'error');
        }
    }
}

// Emergency Features
async function loadEmergencyContacts() {
    const usersResponse = await userService.getAllUsers();
    const contactsContainer = document.getElementById('emergencyContactsList');
    
    if (usersResponse.status === 'SUCCESS') {
        let allEmergencyContacts = [];
        
        for (const user of usersResponse.data) {
            const emergencyResponse = await familyService.getEmergencyContacts(user.id);
            if (emergencyResponse.status === 'SUCCESS') {
                allEmergencyContacts = allEmergencyContacts.concat(
                    emergencyResponse.data.map(contact => ({
                        ...contact,
                        userName: user.name
                    }))
                );
            }
        }
        
        if (allEmergencyContacts.length > 0) {
            contactsContainer.innerHTML = allEmergencyContacts.map(contact => `
                <div class="contact-card">
                    <h4>${contact.name}</h4>
                    <p><strong>Relationship:</strong> ${contact.relationship}</p>
                    <p><strong>Phone:</strong> ${contact.phone}</p>
                    <p><strong>User:</strong> ${contact.userName}</p>
                    ${contact.email ? `<p><strong>Email:</strong> ${contact.email}</p>` : ''}
                </div>
            `).join('');
        } else {
            contactsContainer.innerHTML = '<p>No emergency contacts found</p>';
        }
    }
}

function triggerEmergency() {
    if (confirm('🚨 Are you sure you want to trigger an emergency alert? This will notify all your emergency contacts.')) {
        // In a real app, this would send alerts to emergency contacts
        showToast('Emergency alert sent to all contacts!', 'success');
        
        // Visual emergency effect
        document.body.style.animation = 'emergencyFlash 0.5s infinite';
        setTimeout(() => {
            document.body.style.animation = '';
        }, 3000);
    }
}

function callPolice() {
    showToast('Calling Police (100)...', 'info');
    // In a real app, this would initiate a phone call
}

function callAmbulance() {
    showToast('Calling Ambulance (108)...', 'info');
    // In a real app, this would initiate a phone call
}

function shareLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
            const { latitude, longitude } = position.coords;
            showToast(`Location shared! Lat: ${latitude.toFixed(4)}, Lng: ${longitude.toFixed(4)}`, 'success');
            // In a real app, this would share location with emergency contacts
        }, (error) => {
            showToast('Failed to get location. Please enable location services.', 'error');
        });
    } else {
        showToast('Geolocation is not supported by this browser.', 'error');
    }
}

// Utility Functions
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Add emergency flash animation to CSS
const style = document.createElement('style');
style.textContent = `
    @keyframes emergencyFlash {
        0%, 100% { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        50% { background: #ff4757; }
    }
    
    .badge {
        padding: 0.25rem 0.5rem;
        border-radius: 15px;
        font-size: 0.8rem;
        font-weight: 500;
    }
    
    .badge.danger {
        background: var(--danger);
        color: var(--white);
    }
    
    .text-center {
        text-align: center;
    }
`;
document.head.appendChild(style);