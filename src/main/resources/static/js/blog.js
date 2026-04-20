document.addEventListener('DOMContentLoaded', function () {

    // ── AOS ──────────────────────────────────────────────
    if (typeof AOS !== 'undefined') {
        AOS.init({ duration: 750, once: true, offset: 80 });
    }

    // ── LOADING BAR en navegación interna ────────────────
    const loadingBar = document.getElementById('blogLoadingBar');

    document.querySelectorAll('a[href^="/"]').forEach(link => {
        link.addEventListener('click', function () {
            const href = this.getAttribute('href');
            if (!href.startsWith('#') && !href.includes('#') && loadingBar) {
                loadingBar.style.width = '85%';
                setTimeout(() => { loadingBar.style.width = '0'; }, 700);
            }
        });
    });

    window.addEventListener('beforeunload', () => {
        if (loadingBar) loadingBar.style.width = '100%';
    });

    // ── FILTROS: activo por URL actual ───────────────────
    const currentPath = window.location.pathname;
    document.querySelectorAll('.blog-filter-btn').forEach(btn => {
        const href = btn.getAttribute('href');
        if (href && currentPath === href) {
            btn.classList.add('active');
        }
    });

    // ── SMOOTH SCROLL para anclas ─────────────────────────
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const targetId = this.getAttribute('href');
            if (targetId === '#') return;
            e.preventDefault();
            const target = document.querySelector(targetId);
            if (target) {
                window.scrollTo({
                    top: target.offsetTop - 80,
                    behavior: 'smooth'
                });
            }
        });
    });

    // ── HOVER en tarjetas (accesibilidad teclado) ────────
    document.querySelectorAll('.blog-card, .blog-featured-card').forEach(card => {
        card.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                const link = this.querySelector('a.blog-read-btn, a.blog-read-link');
                if (link) link.click();
            }
        });
    });

    // ── PAGINACIÓN: scroll al inicio del grid ────────────
    document.querySelectorAll('.blog-page-link').forEach(link => {
        link.addEventListener('click', function () {
            setTimeout(() => {
                const grid = document.querySelector('.blog-main');
                if (grid) {
                    window.scrollTo({
                        top: grid.offsetTop - 100,
                        behavior: 'smooth'
                    });
                }
            }, 100);
        });
    });

});
