// admin-galeria.js - Drag & drop, previsualización y carga de imágenes

document.addEventListener('DOMContentLoaded', function() {
    const uploadZone = document.getElementById('uploadZone');
    const fileInput = document.getElementById('imagenFile');
    const previewContainer = document.getElementById('previewContainer');

    if (!uploadZone || !fileInput) return;

    // 1. Abrir selector de archivos al hacer clic en la zona
    uploadZone.addEventListener('click', () => fileInput.click());

    // 2. Prevenir comportamiento por defecto para drag & drop
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        uploadZone.addEventListener(eventName, preventDefaults, false);
    });

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    // 3. Resaltar zona al arrastrar
    ['dragenter', 'dragover'].forEach(eventName => {
        uploadZone.addEventListener(eventName, () => uploadZone.classList.add('drag-over'), false);
    });

    ['dragleave', 'drop'].forEach(eventName => {
        uploadZone.addEventListener(eventName, () => uploadZone.classList.remove('drag-over'), false);
    });

    // 4. Procesar archivos soltados
    uploadZone.addEventListener('drop', function(e) {
        const files = e.dataTransfer.files;
        if (files.length) {
            fileInput.files = files;
            // Disparar evento 'change' para mostrar preview
            const changeEvent = new Event('change', { bubbles: true });
            fileInput.dispatchEvent(changeEvent);
        }
    });

    // 5. Previsualizar imagen cuando se selecciona un archivo
    fileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (!file) {
            if (previewContainer) previewContainer.innerHTML = '';
            return;
        }

        // Validar tipo
        const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
        if (!allowedTypes.includes(file.type)) {
            alert('Formato no soportado. Usa JPG, PNG, GIF o WEBP.');
            fileInput.value = '';
            if (previewContainer) previewContainer.innerHTML = '';
            return;
        }

        // Validar tamaño (máximo 50MB)
        if (file.size > 50 * 1024 * 1024) {
            alert('El archivo excede 50MB. Por favor, elige una imagen más pequeña.');
            fileInput.value = '';
            if (previewContainer) previewContainer.innerHTML = '';
            return;
        }

        // Mostrar preview
        const reader = new FileReader();
        reader.onload = function(ev) {
            if (previewContainer) {
                previewContainer.innerHTML = `
                    <div class="preview-container">
                        <img src="${ev.target.result}" class="preview-image" alt="Vista previa">
                        <div class="text-center text-xs text-gray-500 mt-1">${file.name}</div>
                    </div>
                `;
            }
        };
        reader.readAsDataURL(file);
    });

    // 6. Si ya hay una imagen en edición (editar), mostrar la actual (opcional)
    const imagenActual = document.querySelector('img.preview-image');
    if (imagenActual && previewContainer && !fileInput.files.length) {
        // Ya se ve la imagen actual, no hacer nada
    }
});