class FamilyService {
    // Create family member
    async createFamilyMember(familyData) {
        try {
            const response = await fetch(`${API_BASE_URL}/family`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(familyData)
            });
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to add family member' };
        }
    }

    // Get family members by user ID
    async getFamilyMembersByUserId(userId) {
        try {
            const response = await fetch(`${API_BASE_URL}/family/user/${userId}`);
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to fetch family members' };
        }
    }

    // Get family member by ID
    async getFamilyMemberById(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/family/${id}`);
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to fetch family member' };
        }
    }

    // Update family member
    async updateFamilyMember(id, familyData) {
        try {
            const response = await fetch(`${API_BASE_URL}/family/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(familyData)
            });
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to update family member' };
        }
    }

    // Delete family member
    async deleteFamilyMember(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/family/${id}`, {
                method: 'DELETE'
            });
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to delete family member' };
        }
    }

    // Get emergency contacts by user ID
    async getEmergencyContacts(userId) {
        try {
            const response = await fetch(`${API_BASE_URL}/family/emergency/${userId}`);
            return await response.json();
        } catch (error) {
            return { status: 'ERROR', message: 'Failed to fetch emergency contacts' };
        }
    }
}