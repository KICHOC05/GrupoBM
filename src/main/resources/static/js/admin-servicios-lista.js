// ── Filtrar tabla ────────────────────────────────
function filtrarServicios() {
    const q = document.getElementById('searchInput').value.toLowerCase();
    document.querySelectorAll('.servicio-row').forEach(row => {
        const titulo = row.querySelector('.servicio-titulo')?.textContent.toLowerCase() || '';
        row.style.display = titulo.includes(q) ? '' : 'none';
    });
}

// ── Modal eliminar ───────────────────────────────
function confirmarEliminar(btn) {
    const id     = btn.dataset.id;
    const titulo = btn.dataset.titulo;
    document.getElementById('modalServicioNombre').textContent = titulo;
    document.getElementById('formEliminar').action = `/admin/servicios/eliminar/${id}`;
    document.getElementById('modalEliminar').style.display = 'flex';
}

function cerrarModal() {
    document.getElementById('modalEliminar').style.display = 'none';
}

// Cerrar modal al hacer clic fuera
document.getElementById('modalEliminar')?.addEventListener('click', function(e) {
    if (e.target === this) cerrarModal();
});

// Cerrar con Escape
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') cerrarModal();
});

// ── Auto-ocultar alertas ─────────────────────────
document.querySelectorAll('.alert-admin').forEach(alert => {
    setTimeout(() => {
        alert.style.transition = 'opacity 0.5s';
        alert.style.opacity = '0';
        setTimeout(() => alert.remove(), 500);
    }, 4000);
});