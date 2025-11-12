// Password visibility toggle
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const icon = input.nextElementSibling.querySelector('i');
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// Password strength indicator
function checkPasswordStrength(password) {
    let strength = 0;
    const strengthBar = document.querySelector('.strength-bar');
    const strengthText = document.querySelector('.strength-text');
    
    if (!strengthBar) return;
    
    if (password.length >= 8) strength++;
    if (password.match(/[a-z]+/)) strength++;
    if (password.match(/[A-Z]+/)) strength++;
    if (password.match(/[0-9]+/)) strength++;
    if (password.match(/[$@#&!]+/)) strength++;
    
    const colors = ['#ff4757', '#ffa502', '#ffa502', '#2ed573', '#2ed573'];
    const texts = ['Very Weak', 'Weak', 'Fair', 'Good', 'Strong'];
    
    strengthBar.style.width = `${(strength / 5) * 100}%`;
    strengthBar.style.background = colors[strength - 1] || '#ff4757';
    strengthText.textContent = texts[strength - 1] || 'Very Weak';
}

// Form validation
document.addEventListener('DOMContentLoaded', function() {
    // Password strength check
    const passwordInput = document.getElementById('password');
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            checkPasswordStrength(this.value);
        });
    }
    
    // Confirm password validation
    const confirmPassword = document.getElementById('confirmPassword');
    const familyConfirmPassword = document.getElementById('familyConfirmPassword');
    
    if (confirmPassword) {
        confirmPassword.addEventListener('input', function() {
            const password = document.getElementById('password').value;
            if (this.value !== password) {
                this.style.borderColor = '#ff4757';
            } else {
                this.style.borderColor = '#2ed573';
            }
        });
    }
    
    if (familyConfirmPassword) {
        familyConfirmPassword.addEventListener('input', function() {
            const password = document.getElementById('familyPassword').value;
            if (this.value !== password) {
                this.style.borderColor = '#ff4757';
            } else {
                this.style.borderColor = '#2ed573';
            }
        });
    }
    
    // Form submission
    const signupForm = document.getElementById('signupForm');
    if (signupForm) {
        signupForm.addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Passwords do not match!');
                return false;
            }
            
            if (password.length < 8) {
                e.preventDefault();
                alert('Password must be at least 8 characters long!');
                return false;
            }
        });
    }
    
    const familySignupForm = document.getElementById('familySignupForm');
    if (familySignupForm) {
        familySignupForm.addEventListener('submit', function(e) {
            const password = document.getElementById('familyPassword').value;
            const confirmPassword = document.getElementById('familyConfirmPassword').value;
            
            if (password !== confirmPassword) {
                e.preventDefault();
                alert('Passwords do not match!');
                return false;
            }
            
            if (password.length < 8) {
                e.preventDefault();
                alert('Password must be at least 8 characters long!');
                return false;
            }
        });
    }
});

// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Mobile menu toggle
document.addEventListener('DOMContentLoaded', function() {
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    const navMenu = document.querySelector('.nav-menu');
    
    if (mobileMenuBtn && navMenu) {
        mobileMenuBtn.addEventListener('click', function() {
            navMenu.classList.toggle('active');
        });
    }
});

// Toast notification system
function showToast(message, type = 'info') {
    // Create toast element
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;
    toast.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 1rem 2rem;
        border-radius: 8px;
        color: white;
        font-weight: 500;
        z-index: 1001;
        transform: translateX(400px);
        transition: transform 0.3s ease;
    `;
    
    if (type === 'success') toast.style.background = '#2ed573';
    else if (type === 'error') toast.style.background = '#ff4757';
    else toast.style.background = '#1e90ff';
    
    document.body.appendChild(toast);
    
    // Animate in
    setTimeout(() => {
        toast.style.transform = 'translateX(0)';
    }, 100);
    
    // Remove after 3 seconds
    setTimeout(() => {
        toast.style.transform = 'translateX(400px)';
        setTimeout(() => {
            document.body.removeChild(toast);
        }, 300);
    }, 3000);
}