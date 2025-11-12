const API_BASE_URL = 'http://localhost:8080/api';

class UserService {
    // Create user
    async createUser(userData) {
        try {
            const response = await fetch(`${API_BASE_URL}/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to create user' };
        }
    }

    // Get all users
    async getAllUsers() {
        try {
            const response = await fetch(`${API_BASE_URL}/users`);
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to fetch users' };
        }
    }

    // Get user by ID
    async getUserById(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/users/${id}`);
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to fetch user' };
        }
    }

    // Update user
    async updateUser(id, userData) {
        try {
            const response = await fetch(`${API_BASE_URL}/users/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to update user' };
        }
    }

    // Delete user
    async deleteUser(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/users/${id}`, {
                method: 'DELETE'
            });
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to delete user' };
        }
    }

    // Search users by name
    async searchUsers(name) {
        try {
            const response = await fetch(`${API_BASE_URL}/users/search?name=${encodeURIComponent(name)}`);
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to search users' };
        }
    }
}