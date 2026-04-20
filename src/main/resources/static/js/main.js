// ========================================
// MENÚ MÓVIL
// ========================================
function toggleMobileMenu() {
    const menu    = document.getElementById('mobileMenu');
    const overlay = document.getElementById('mobileMenuOverlay');
    const toggle  = document.getElementById('menuToggle');
    if (!menu || !overlay || !toggle) return;
    menu.classList.toggle('active');
    overlay.classList.toggle('active');
    toggle.classList.toggle('active');
    document.body.style.overflow = menu.classList.contains('active') ? 'hidden' : 'auto';
}

function closeMobileMenu() {
    const menu    = document.getElementById('mobileMenu');
    const overlay = document.getElementById('mobileMenuOverlay');
    const toggle  = document.getElementById('menuToggle');
    if (!menu || !overlay || !toggle) return;
    menu.classList.remove('active');
    overlay.classList.remove('active');
    toggle.classList.remove('active');
    document.body.style.overflow = 'auto';
}

// ========================================
// MODAL DE SERVICIOS (original + nueva función por ID)
// ========================================
function openServiceModal(service) {
    const modal   = document.getElementById('serviceModal');
    const content = document.getElementById('serviceModalContent');
    if (!modal || !content) return;

    const features = service.features.map(f =>
        `<li class="flex items-center"><i class="fas fa-check-circle text-[#8B0000] mr-3"></i>${f}</li>`
    ).join('');

    const images = service.images || (service.image ? [service.image] : []);
    const slides = images.map(img =>
        `<img src="${img}" class="modal-slide" onerror="this.src='https://via.placeholder.com/800x600?text=Imagen'">`
    ).join('');
    const dots = images.map((_, i) =>
        `<span class="${i === 0 ? 'active' : ''}" onclick="goToModalSlide(${i})"></span>`
    ).join('');

    content.innerHTML = `
        <div class="grid md:grid-cols-2 gap-8 items-center">
            <div>
                <div class="modal-slider" id="modalSlider">
                    <div class="modal-slider-track" id="modalTrack" data-current="0">${slides}</div>
                    ${images.length > 1 ? `
                    <button class="modal-slider-btn prev" onclick="slideModal(-1)"><i class="fas fa-chevron-left"></i></button>
                    <button class="modal-slider-btn next" onclick="slideModal(1)"><i class="fas fa-chevron-right"></i></button>
                    <div class="modal-slider-dots" id="modalDots">${dots}</div>` : ''}
                </div>
            </div>
            <div>
			<img src="/img/Logo_Cortes.png" alt="Grupo BM" style="height:70px;width:auto;object-fit:contain;margin-bottom:1.5rem;filter:brightness(0) invert(1);">
                <h2 class="text-4xl font-bold mb-4">${service.title}</h2>
                <p class="text-xl mb-6 text-gray-300">${service.description}</p>
                <div class="bg-[#8B0000] p-4 rounded-xl mb-6">
                    <p class="text-2xl font-bold">${service.price}</p>
                </div>
                <h3 class="text-xl font-bold mb-3">Características:</h3>
                <ul class="space-y-3 mb-8">${features}</ul>
                <div class="flex gap-4 flex-wrap">
                    <a href="https://wa.me/525644091155?text=Hola%2C%20me%20interesa%20el%20servicio%20de%20${encodeURIComponent(service.title)}"
                       class="btn-vino text-xl px-8 py-4" target="_blank">
                        <i class="fab fa-whatsapp mr-2"></i>Cotizar por WhatsApp
                    </a>
                    <button onclick="closeServiceModal()"
                            class="border-2 border-white text-white px-8 py-4 rounded-lg hover:bg-white hover:text-[#2C0101] transition">
                        Seguir viendo
                    </button>
                </div>
            </div>
        </div>`;

    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

// NUEVA FUNCIÓN: Abre el modal cargando los datos del servicio por ID (AJAX)
async function openServiceModalById(element) {
    const serviceId = element.getAttribute('data-id');
    if (!serviceId) return;

    try {
        const response = await fetch(`/api/servicios/${serviceId}`);
        if (!response.ok) throw new Error('Error al cargar el servicio');
        const service = await response.json();

        const modalData = {
            title: service.titulo,
            description: service.descripcion,
            icon: "fa-cogs",
            features: service.caracteristicasList || [],
            images: service.imagenesList || [],
            price: service.mensaje || "Contáctanos para más información"
        };
        openServiceModal(modalData);
    } catch (error) {
        console.error('Error al cargar el servicio:', error);
        mostrarNotificacion('No se pudo cargar el servicio', 'error');
    }
}

function closeServiceModal() {
    const modal = document.getElementById('serviceModal');
    if (!modal) return;
    modal.classList.remove('active');
    document.body.style.overflow = 'auto';
}

function slideModal(direction) {
    const track = document.getElementById('modalTrack');
    const dots  = document.querySelectorAll('#modalDots span');
    if (!track) return;
    const total = track.children.length;
    let current = parseInt(track.dataset.current || '0');
    current = (current + direction + total) % total;
    track.dataset.current = current;
    track.style.transform = `translateX(-${current * 100}%)`;
    dots.forEach((d, i) => d.classList.toggle('active', i === current));
}

function goToModalSlide(index) {
    const track = document.getElementById('modalTrack');
    const dots  = document.querySelectorAll('#modalDots span');
    if (!track) return;
    track.dataset.current = index;
    track.style.transform  = `translateX(-${index * 100}%)`;
    dots.forEach((d, i) => d.classList.toggle('active', i === index));
}

// ========================================
// GALERÍA DINÁMICA (cargada desde API)
// ========================================
let galeriaData = [];

async function cargarGaleria() {
    try {
        const response = await fetch('/api/galeria');
        if (!response.ok) throw new Error('Error al cargar galería');
        galeriaData = await response.json();
        renderGaleria();
    } catch (error) {
        console.error('Error al cargar galería:', error);
        const grid = document.getElementById('galeriaGrid');
        if (grid) grid.innerHTML = '<p class="text-center text-gray-500 col-span-full">No se pudieron cargar las imágenes</p>';
    }
}

function renderGaleria() {
    const grid = document.getElementById('galeriaGrid');
    if (!grid) return;
    if (!galeriaData.length) {
        grid.innerHTML = '<p class="text-center text-gray-500 col-span-full">Próximamente más imágenes</p>';
        return;
    }
    grid.innerHTML = galeriaData.map((img, index) => `
        <div class="gallery-item" onclick="openLightbox(${index})" data-aos="fade-up" data-aos-delay="${(index % 10) * 50}">
            <img src="${img.imagenUrl}" alt="${img.descripcion}" onerror="this.src='https://via.placeholder.com/600x400?text=Error'">
            <div class="gallery-overlay"><p>${img.descripcion}</p></div>
        </div>
    `).join('');
    if (typeof AOS !== 'undefined') AOS.refresh();
}

let lightboxIndex = 0;

function openLightbox(index) {
    lightboxIndex = index;
    updateLightbox();
    document.getElementById('galleryLightbox').classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeLightbox() {
    document.getElementById('galleryLightbox').classList.remove('active');
    document.body.style.overflow = 'auto';
}

function navLightbox(dir) {
    if (!galeriaData.length) return;
    lightboxIndex = (lightboxIndex + dir + galeriaData.length) % galeriaData.length;
    updateLightbox();
}

function updateLightbox() {
    const img = galeriaData[lightboxIndex];
    if (img) {
        document.getElementById('lightboxImg').src = img.imagenUrl;
        document.getElementById('lightboxCaption').textContent = img.descripcion;
    }
}

// ========================================
// CLIENTES (carrusel dinámico desde API)
// ========================================
async function cargarClientes() {
    try {
        const response = await fetch('/api/clientes');
        if (!response.ok) throw new Error('Error al cargar clientes');
        const clientes = await response.json();
        renderClientes(clientes);
    } catch (error) {
        console.error('Error al cargar clientes:', error);
        const wrapper = document.getElementById('clientesSwiperWrapper');
        if (wrapper) wrapper.innerHTML = '<div class="text-center text-gray-500">No se pudieron cargar los clientes</div>';
    }
}

function renderClientes(clientes) {
    const wrapper = document.getElementById('clientesSwiperWrapper');
    if (!wrapper) return;
    
    if (!clientes.length) {
        wrapper.innerHTML = '<div class="text-center text-gray-500">Próximamente más clientes</div>';
        return;
    }
    
    // Limpiar y llenar el swiper-wrapper
    wrapper.innerHTML = clientes.map(c => `
        <div class="swiper-slide">
            <img src="${c.imagenUrl}" alt="${c.nombre}" class="client-logo" onerror="this.src='https://via.placeholder.com/150x80?text=Logo'">
        </div>
    `).join('');
    
    // Reinicializar Swiper (destruir instancia anterior si existe)
    if (window.clientSwiperInstance) {
        window.clientSwiperInstance.destroy(true, true);
    }
    
    // Esperar un momento para que el DOM se actualice y luego inicializar Swiper
    setTimeout(() => {
        if (typeof Swiper !== 'undefined' && document.querySelector('.client-swiper')) {
            window.clientSwiperInstance = new Swiper('.client-swiper', {
                slidesPerView: 2,
                spaceBetween: 30,
                loop: true,
                autoplay: { delay: 2500, disableOnInteraction: false },
                pagination: { el: '.swiper-pagination', clickable: true },
                breakpoints: {
                    480:  { slidesPerView: 3 },
                    640:  { slidesPerView: 4 },
                    768:  { slidesPerView: 5 },
                    1024: { slidesPerView: 6 }
                }
            });
        } else {
            console.warn('Swiper no está disponible o el contenedor no existe');
        }
    }, 100);
}


// ========================================
// FORMULARIO WHATSAPP (solo nombre, email y mensaje)
// ========================================
function sendViaWhatsApp(event) {
    event.preventDefault();
    const nombre = document.getElementById('nombre')?.value || '';
    const email  = document.getElementById('email')?.value  || '';
    const mensaje = document.getElementById('mensaje')?.value || '';

    const text = `Hola Grupo BM,%0A%0A*Nuevo mensaje de contacto:*%0A%0A👤 *Nombre:* ${nombre}%0A📧 *Email:* ${email}%0A📝 *Mensaje:* ${mensaje}`;
    window.open(`https://wa.me/525644091155?text=${text}`, '_blank');
}
// ========================================
// CONTADORES ANIMADOS
// ========================================
function initCounters() {
    const counters = document.querySelectorAll('.counter-number');
    if (!counters.length) return;
    counters.forEach(c => { c.innerText = '0'; });

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (!entry.isIntersecting) return;
            const counter = entry.target;
            const target  = parseInt(counter.dataset.target);
            const prefix  = counter.dataset.prefix || '';
            let current   = 0;
            const timer   = setInterval(() => {
                current++;
                if (current >= target) { counter.innerText = prefix + target; clearInterval(timer); }
                else { counter.innerText = prefix + current; }
            }, 60);
            observer.unobserve(counter);
        });
    }, { threshold: 0.3 });

    counters.forEach(c => observer.observe(c));
}

// ========================================
// SMOOTH SCROLL
// ========================================
function initSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
                closeMobileMenu();
            }
        });
    });
}

// ========================================
// NOTIFICACIÓN SIMPLE (auxiliar)
// ========================================
function mostrarNotificacion(mensaje, tipo) {
    console.log(`[${tipo}] ${mensaje}`);
    alert(mensaje);
}

// ========================================
// INICIALIZACIÓN
// ========================================
document.addEventListener('DOMContentLoaded', function () {
    if (typeof AOS !== 'undefined') AOS.init({ duration: 800, once: true });
    setTimeout(initCounters, 500);
    initSmoothScroll();
    cargarGaleria();
    cargarClientes();   // ← NUEVO: carga los clientes dinámicamente
});

window.addEventListener('load', function () {
    // El Swiper se inicializa dentro de renderClientes, ya no es necesario llamarlo aquí.
    // Si no hay clientes, igualmente no pasa nada.
});

window.addEventListener('resize', () => {
    if (window.innerWidth > 768) closeMobileMenu();
});

document.addEventListener('keydown', (e) => {
    const lightboxOpen = document.getElementById('galleryLightbox')?.classList.contains('active');
    if (lightboxOpen) {
        if (e.key === 'ArrowRight') navLightbox(1);
        if (e.key === 'ArrowLeft')  navLightbox(-1);
        if (e.key === 'Escape')     closeLightbox();
        return;
    }
    if (e.key === 'Escape') { closeServiceModal(); closeMobileMenu(); }
});