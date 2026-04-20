// blog-detalle.js - Funcionalidades para la página de detalle de artículo

document.addEventListener('DOMContentLoaded', function() {
    // Calcular tiempo de lectura
    function calcularTiempoLectura() {
        const content = document.getElementById('articleContent');
        if (content) {
            const texto = content.innerText;
            const palabras = texto.trim().split(/\s+/).length;
            const minutos = Math.ceil(palabras / 200); // 200 palabras por minuto
            const span = document.getElementById('readingTime');
            if (span) {
                span.textContent = minutos <= 1 ? 'Lectura de 1 min' : `Lectura de ${minutos} min`;
            }
        }
    }
    setTimeout(calcularTiempoLectura, 100);

    // Compartir en redes sociales
    const shareBtns = document.querySelectorAll('.social-share-btn');
    shareBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const url = encodeURIComponent(window.location.href);
            const titulo = encodeURIComponent(document.title);
            let shareUrl = '';
            if (this.classList.contains('share-facebook')) {
                shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${url}`;
            } else if (this.classList.contains('share-twitter')) {
                shareUrl = `https://twitter.com/intent/tweet?url=${url}&text=${titulo}`;
            } else if (this.classList.contains('share-whatsapp')) {
                shareUrl = `https://wa.me/?text=${titulo}%20${url}`;
            } else if (this.classList.contains('share-linkedin')) {
                shareUrl = `https://www.linkedin.com/shareArticle?mini=true&url=${url}&title=${titulo}`;
            }
            if (shareUrl) window.open(shareUrl, '_blank', 'width=600,height=400');
        });
    });

    // Inicializar AOS (si no se ha hecho ya en otro script)
    if (typeof AOS !== 'undefined') {
        AOS.init({ duration: 800, once: true, offset: 50 });
    }
});