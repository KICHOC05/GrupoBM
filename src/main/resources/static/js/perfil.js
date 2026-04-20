// perfil.js - Funcionalidades específicas del perfil

let isLoading = false;

document.addEventListener('DOMContentLoaded', function() {
    cargarDatosUsuario();
    inicializarEventListeners();
});

function inicializarEventListeners() {
    const profileForm = document.getElementById('profileForm');
    if (profileForm) {
        profileForm.addEventListener('submit', actualizarPerfil);
    }
}

function cargarDatosUsuario() {
    mostrarLoading(true);
    
    fetch('/admin/api/usuario-actual')
        .then(response => response.json())
        .then(data => {
            if (data.success && data.user) {
                const user = data.user;
                
                // Llenar formulario
                document.getElementById('nombre').value = user.nombre || '';
                document.getElementById('apellido').value = user.apellido || '';
                document.getElementById('email').value = user.email || '';
                document.getElementById('telefono').value = user.telefono || '';
                document.getElementById('direccion').value = user.direccion || '';
                
                // Actualizar header del perfil
                const nombreCompleto = `${user.nombre || ''} ${user.apellido || ''}`.trim();
                document.getElementById('userFullName').textContent = nombreCompleto || 'Usuario';
                document.getElementById('userEmail').textContent = user.email || '-';
                
                const rolTexto = user.rol === 'ROLE_ADMIN' ? 'Administrador' : 'Usuario';
                document.getElementById('userRole').textContent = rolTexto;
                
                // Avatar inicial
                const inicial = (user.nombre && user.nombre.length > 0) ? user.nombre.charAt(0).toUpperCase() : 'U';
                document.getElementById('avatarInitial').textContent = inicial;
                
                // Estadísticas
                document.getElementById('userId').textContent = `#${user.id || '-'}`;
                document.getElementById('userRoleValue').textContent = rolTexto;
                
                if (user.createdAt) {
                    const fechaRegistro = new Date(user.createdAt);
                    document.getElementById('fechaRegistro').textContent = fechaRegistro.toLocaleDateString('es-MX');
                    
                    // Calcular días activo
                    const hoy = new Date();
                    const diffTime = Math.abs(hoy - fechaRegistro);
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                    document.getElementById('diasActivo').textContent = diffDays;
                }
                
                // Datos simulados (puedes conectar con backend real)
                document.getElementById('ultimoAcceso').textContent = 'Hoy';
                document.getElementById('totalAccesos').textContent = '42';
                
            } else {
                mostrarNotificacion(data.message || 'Error al cargar datos', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            mostrarNotificacion('Error de conexión al servidor', 'error');
        })
        .finally(() => {
            mostrarLoading(false);
        });
}

function actualizarPerfil(event) {
    event.preventDefault();
    
    if (isLoading) return;
    
    const userData = {
        nombre: document.getElementById('nombre').value.trim(),
        apellido: document.getElementById('apellido').value.trim(),
        email: document.getElementById('email').value.trim(),
        telefono: document.getElementById('telefono').value.trim(),
        direccion: document.getElementById('direccion').value.trim()
    };
    
    if (!userData.nombre || !userData.apellido || !userData.email) {
        mostrarNotificacion('Nombre, apellido y email son obligatorios', 'error');
        return;
    }
    
    if (!validarEmail(userData.email)) {
        mostrarNotificacion('Email no válido', 'error');
        return;
    }
    
    mostrarLoading(true);
    
    fetch('/admin/api/perfil/actualizar', {
        method: 'PUT',
        headers: { 
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            mostrarNotificacion('Perfil actualizado correctamente', 'success');
            setTimeout(() => location.reload(), 1500);
        } else {
            mostrarNotificacion(data.message || 'Error al actualizar', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        mostrarNotificacion('Error de conexión', 'error');
    })
    .finally(() => {
        mostrarLoading(false);
    });
}

function validarEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

function mostrarNotificacion(mensaje, tipo = 'info') {
    const notification = document.createElement('div');
    notification.className = `fixed top-20 right-4 z-50 px-5 py-4 rounded-xl shadow-lg text-white ${tipo === 'success' ? 'bg-green-500' : 'bg-red-500'} transition-all duration-300`;
    notification.innerHTML = `
        <div class="flex items-center gap-3">
            <i class="fas ${tipo === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'} text-lg"></i>
            <span class="text-sm font-medium">${mensaje}</span>
        </div>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => notification.remove(), 300);
    }, 4000);
}

function mostrarLoading(show) {
    isLoading = show;
    const profileBtn = document.querySelector('#profileForm button[type="submit"]');
    
    if (profileBtn) {
        if (show) {
            profileBtn.disabled = true;
            profileBtn.innerHTML = '<div class="loading-spinner"></div> Guardando...';
        } else {
            profileBtn.disabled = false;
            profileBtn.innerHTML = '<i class="fas fa-save"></i> Guardar Cambios';
        }
    }
}