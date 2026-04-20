// admin-servicios-form.js - Manejo de características como frases completas

// ============================================
// MANEJO DE CARACTERÍSTICAS (SOLO FRASES)
// ============================================

function agregarCaracteristicaDesdeFrase() {
    const texto = document.getElementById('nuevaCaracteristica').value;
    if (!texto.trim()) {
        mostrarNotificacion('Escribe una frase para la característica', 'info');
        return;
    }
    
    const container = document.getElementById('caracteristicasContainer');
    const index = container.children.length;
    const div = document.createElement('div');
    div.className = 'carac-row';
    div.innerHTML = `
        <div class="carac-number">${index + 1}</div>
        <input type="text" 
               name="caracteristicasList" 
               class="form-control-admin" 
               value="${escapeHtml(texto.trim())}"
               placeholder="Característica"
               style="flex:1;">
        <button type="button" class="btn-remove-carac" onclick="eliminarCaracteristica(this)">
            <i class="fas fa-trash-alt"></i>
        </button>
    `;
    container.appendChild(div);
    actualizarNumerosCaracteristicas();
    
    // Limpiar textarea y enfocar
    document.getElementById('nuevaCaracteristica').value = '';
    mostrarNotificacion('Característica agregada correctamente', 'success');
    document.getElementById('nuevaCaracteristica').focus();
}

function eliminarCaracteristica(btn) {
    const row = btn.closest('.carac-row');
    if (row && document.querySelectorAll('.carac-row').length > 1) {
        row.remove();
        actualizarNumerosCaracteristicas();
    } else {
        mostrarNotificacion('Debe haber al menos una característica', 'error');
    }
}

function actualizarNumerosCaracteristicas() {
    const rows = document.querySelectorAll('.carac-row');
    rows.forEach((row, idx) => {
        const numero = row.querySelector('.carac-number');
        if (numero) numero.textContent = idx + 1;
    });
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>]/g, function(m) {
        if (m === '&') return '&amp;';
        if (m === '<') return '&lt;';
        if (m === '>') return '&gt;';
        return m;
    });
}

// ============================================
// MANEJO DE IMÁGENES (sin cambios)
// ============================================

let imagenesSeleccionadas = [];

function configurarSubidaImagenes() {
    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('imagenesFile');
    if (!dropZone || !fileInput) return;

    dropZone.addEventListener('click', () => fileInput.click());

    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.classList.add('drag-over');
    });
    dropZone.addEventListener('dragleave', () => {
        dropZone.classList.remove('drag-over');
    });
    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.classList.remove('drag-over');
        const files = Array.from(e.dataTransfer.files);
        procesarArchivos(files);
    });

    fileInput.addEventListener('change', (e) => {
        procesarArchivos(Array.from(e.target.files));
        fileInput.value = '';
    });
}

function procesarArchivos(files) {
    const maxSize = 50 * 1024 * 1024;
    const formatosPermitidos = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
    let nuevosArchivos = [];
    let errores = [];

    for (let file of files) {
        if (file.size > maxSize) {
            errores.push(`${file.name}: excede 50MB`);
            continue;
        }
        if (!formatosPermitidos.includes(file.type)) {
            errores.push(`${file.name}: formato no permitido`);
            continue;
        }
        nuevosArchivos.push(file);
    }
    if (errores.length > 0) mostrarNotificacion(errores.join('\n'), 'error');
    if (nuevosArchivos.length > 0) {
        imagenesSeleccionadas.push(...nuevosArchivos);
        actualizarPreviews();
    }
}

function actualizarPreviews() {
    const container = document.getElementById('nuevasPreviews');
    if (!container) return;
    if (imagenesSeleccionadas.length === 0) {
        container.innerHTML = '<p class="text-gray-400 text-sm">No hay imágenes seleccionadas</p>';
        return;
    }
    container.innerHTML = '';
    imagenesSeleccionadas.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = (e) => {
            const div = document.createElement('div');
            div.className = 'nueva-preview-item';
            div.innerHTML = `
                <img src="${e.target.result}" class="nueva-preview-img" alt="Preview">
                <div class="nueva-preview-name">${file.name.substring(0, 20)}</div>
                <button type="button" class="btn-remove-nueva" onclick="eliminarImagenSeleccionada(${index})">
                    <i class="fas fa-times"></i>
                </button>
            `;
            container.appendChild(div);
        };
        reader.readAsDataURL(file);
    });
}

function eliminarImagenSeleccionada(index) {
    imagenesSeleccionadas.splice(index, 1);
    actualizarPreviews();
}

function eliminarImagenExistente(url, elemento) {
    const input = document.getElementById('imagenesEliminar');
    if (input) {
        let existentes = input.value ? input.value.split(',') : [];
        if (!existentes.includes(url)) {
            existentes.push(url);
            input.value = existentes.join(',');
        }
    }
    const item = elemento.closest('.imagen-actual-item');
    if (item) {
        item.classList.add('removed');
        elemento.disabled = true;
        elemento.innerHTML = '<i class="fas fa-check"></i>';
        mostrarNotificacion('Imagen marcada para eliminar', 'info');
    }
}

// ============================================
// ENVÍO DEL FORMULARIO
// ============================================

function prepararFormulario() {
    const form = document.getElementById('servicioForm');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const formData = new FormData();

        formData.append('id', document.getElementById('id')?.value || '0');
        formData.append('titulo', document.getElementById('titulo').value);
        formData.append('descripcion', document.getElementById('descripcion').value);
        formData.append('mensaje', document.getElementById('mensaje')?.value || '');
        formData.append('activo', document.getElementById('activo')?.checked || false);

        // Características: cada input con name="caracteristicasList"
        const inputsCarac = document.querySelectorAll('input[name="caracteristicasList"]');
        inputsCarac.forEach(input => {
            const val = input.value.trim();
            if (val) {
                formData.append('caracteristicasList', val);
            }
        });

        const eliminarInput = document.getElementById('imagenesEliminar');
        if (eliminarInput && eliminarInput.value) {
            formData.append('imagenesEliminar', eliminarInput.value);
        }

        for (let file of imagenesSeleccionadas) {
            formData.append('imagenesFile', file);
        }

        const btn = form.querySelector('button[type="submit"]');
        const originalText = btn.innerHTML;
        btn.disabled = true;
        btn.innerHTML = '<div class="loading-spinner"></div> Guardando...';

        try {
            const response = await fetch('/admin/servicios/guardar', {
                method: 'POST',
                body: formData
            });

            if (response.redirected) {
                window.location.href = response.url;
            } else {
                const result = await response.json();
                if (result.success) {
                    mostrarNotificacion('Servicio guardado correctamente', 'success');
                    setTimeout(() => window.location.href = '/admin/servicios', 1500);
                } else {
                    mostrarNotificacion(result.error || 'Error al guardar', 'error');
                }
            }
        } catch (error) {
            console.error('Error:', error);
            mostrarNotificacion('Error de conexión', 'error');
        } finally {
            btn.disabled = false;
            btn.innerHTML = originalText;
        }
    });
}

// ============================================
// UTILIDADES
// ============================================

function mostrarNotificacion(mensaje, tipo) {
    const notification = document.createElement('div');
    notification.className = `fixed top-20 right-4 z-50 px-5 py-4 rounded-xl shadow-lg text-white ${
        tipo === 'success' ? 'bg-green-500' : tipo === 'error' ? 'bg-red-500' : 'bg-blue-500'
    } transition-all duration-300`;
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

// Inicializar
document.addEventListener('DOMContentLoaded', () => {
    configurarSubidaImagenes();
    prepararFormulario();

    const btnAgregar = document.getElementById('btnAgregarCaracteristica');
    if (btnAgregar) btnAgregar.addEventListener('click', agregarCaracteristicaDesdeFrase);
});