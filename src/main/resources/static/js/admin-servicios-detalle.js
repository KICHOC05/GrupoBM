// admin-servicios-detalle.js - Funcionalidades para la vista de detalle

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar tooltips o efectos adicionales si son necesarios
    console.log('Vista de detalle de servicio cargada');
    
    // Puedes agregar aquí lógica para abrir imágenes en modal, etc.
    inicializarGaleria();
});

function inicializarGaleria() {
    const imagenes = document.querySelectorAll('.gallery-item img');
    imagenes.forEach(img => {
        img.addEventListener('click', function() {
            // Abrir imagen en una ventana modal simple (opcional)
            const url = this.src;
            window.open(url, '_blank');
        });
        img.style.cursor = 'pointer';
    });
}