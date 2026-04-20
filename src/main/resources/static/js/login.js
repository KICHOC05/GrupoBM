// login.js - Funcionalidades para la página de login

// Toggle para mostrar/ocultar contraseña
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const eyeIcon = document.getElementById('eyeIcon');
    
    if (!passwordInput || !eyeIcon) return;
    
    const isPassword = passwordInput.type === 'password';
    passwordInput.type = isPassword ? 'text' : 'password';
    eyeIcon.className = isPassword ? 'fas fa-eye-slash' : 'fas fa-eye';
}

// Manejar el envío del formulario
function handleLoginSubmit(event) {
    const form = document.getElementById('loginForm');
    if (!form) return;
    
    const btn = document.getElementById('loginBtn');
    const spinner = document.getElementById('loginSpinner');
    const icon = document.getElementById('loginIcon');
    const text = document.getElementById('loginText');
    
    // Deshabilitar botón y mostrar spinner
    if (btn) {
        btn.disabled = true;
    }
    if (spinner) {
        spinner.style.display = 'block';
    }
    if (icon) {
        icon.style.display = 'none';
    }
    if (text) {
        text.textContent = 'Verificando...';
    }
    
    // El formulario se enviará normalmente
    // No necesitamos prevenir el evento porque Spring Security maneja el POST
}

// Ocultar alertas después de 5 segundos
function autoHideAlerts() {
    const errorAlert = document.getElementById('errorAlert');
    const successAlert = document.getElementById('successAlert');
    
    if (errorAlert) {
        setTimeout(() => {
            errorAlert.style.transition = 'opacity 0.5s';
            errorAlert.style.opacity = '0';
            setTimeout(() => {
                if (errorAlert.parentNode) {
                    errorAlert.style.display = 'none';
                }
            }, 500);
        }, 5000);
    }
    
    if (successAlert) {
        setTimeout(() => {
            successAlert.style.transition = 'opacity 0.5s';
            successAlert.style.opacity = '0';
            setTimeout(() => {
                if (successAlert.parentNode) {
                    successAlert.style.display = 'none';
                }
            }, 500);
        }, 5000);
    }
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Enfocar el campo de email
    const usernameField = document.getElementById('username');
    if (usernameField) {
        usernameField.focus();
    }
    
    // Configurar auto-ocultado de alertas
    autoHideAlerts();
    
    // Agregar event listener al formulario
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLoginSubmit);
    }
});

// Función para reintentar login (útil si hay error de conexión)
function retryLogin() {
    const form = document.getElementById('loginForm');
    if (form) {
        form.submit();
    }
}