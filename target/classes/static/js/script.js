/**
 * LUMEN | Artisanal Mixology Interactive Engine
 * Handles dynamic cocktail interactions, filtering, detail modal,
 * AJAX cart operations, toasts, and background bubble animations.
 */

// State Management
let activeCategory = 'all';

// DOM Elements Initialization
document.addEventListener('DOMContentLoaded', () => {
    initBubbles();
    setupFilterListeners();
    setupModalListeners();
    setupMobileNav();
    setupHeaderScroll();
    setupCartActions();
});

// Category Filter Handlers (Filtering DOM cards directly to preserve smooth transitions)
function setupFilterListeners() {
    const filterBtns = document.querySelectorAll('.filter-btn');
    const cards = document.querySelectorAll('.cocktail-card');
    
    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            filterBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const category = btn.getAttribute('data-category');
            activeCategory = category;

            cards.forEach(card => {
                const cardCategory = card.getAttribute('data-category');
                if (category === 'all' || cardCategory === category) {
                    card.style.display = 'flex';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    });
}

// Detail Modal Handler (Pulls data from hidden inputs rendered in each card)
function openDetailModal(id) {
    const card = document.querySelector(`.cocktail-card[data-id="${id}"]`);
    if (!card) return;

    const name = card.querySelector('.cocktail-title').innerText;
    const priceStr = card.querySelector('.cocktail-price').innerText;
    const image = card.querySelector('.cocktail-img').src;
    const fullDesc = card.querySelector('.drink-full-desc').value;
    const citric = card.querySelector('.drink-citric').value;
    const herbal = card.querySelector('.drink-herbal').value;
    const sweet = card.querySelector('.drink-sweet').value;
    const alcohol = card.querySelector('.drink-alcohol').value;
    const ingredientsStr = card.querySelector('.drink-ingredients-str').value;
    const ingredients = ingredientsStr.split(',').map(i => i.trim()).filter(i => i.length > 0);

    const modalBody = document.getElementById('modalBody');
    modalBody.innerHTML = `
        <div style="padding: 1rem 0;">
            <div style="width: 100%; height: 260px; border-radius: var(--radius-md); overflow: hidden; margin-bottom: 1.5rem; background: #000;">
                <img src="${image}" alt="${name}" style="width: 100%; height: 100%; object-fit: cover;" onerror="this.src='/images/ruby_spritz.png'">
            </div>
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.8rem;">
                <h2 style="font-size: 1.8rem;">${name}</h2>
                <span style="font-family: var(--font-heading); font-size: 1.8rem; color: var(--citrus-gold); font-weight: 800;">${priceStr}</span>
            </div>

            <p style="color: var(--text-secondary); margin-bottom: 1.5rem; line-height: 1.6;">${fullDesc}</p>

            <h4 style="font-size: 0.9rem; text-transform: uppercase; color: var(--rosemary-green-light); letter-spacing: 1px; margin-bottom: 0.8rem;">
                <i class="fa-solid fa-list-check"></i> Ingredientes Completos
            </h4>
            <ul style="list-style: none; display: grid; grid-template-columns: 1fr 1fr; gap: 0.6rem; margin-bottom: 1.8rem;">
                ${ingredients.map(ing => `
                    <li style="font-size: 0.9rem; background: rgba(255,255,255,0.05); padding: 0.5rem 0.8rem; border-radius: 8px; border: 1px solid var(--border-glass);">
                        <i class="fa-solid fa-check" style="color: var(--ruby-pink); margin-right: 6px;"></i> ${ing}
                    </li>
                `).join('')}
            </ul>

            <div style="background: rgba(255,255,255,0.03); padding: 1.2rem; border-radius: var(--radius-md); border: 1px solid var(--border-glass); margin-bottom: 1.8rem;">
                <h4 style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 0.8rem;">PERFIL DE SABOR & GRADUACIÓN</h4>
                <div style="display: flex; justify-content: space-between; font-size: 0.85rem; color: var(--text-secondary); flex-wrap: wrap; gap: 10px;">
                    <span>Cítrico: <strong>${citric}%</strong></span>
                    <span>Herbal: <strong>${herbal}%</strong></span>
                    <span>Dulzura: <strong>${sweet}%</strong></span>
                    <span>Alcohol: <strong>${alcohol}</strong></span>
                </div>
            </div>

            <button class="btn btn-primary btn-block btn-lg order-trigger-btn" data-id="${id}" id="modalOrderBtn">
                <i class="fa-solid fa-martini-glass"></i> Añadir al Carrito ${priceStr}
            </button>
        </div>
    `;

    document.getElementById('detailModal').classList.add('active');

    // Bind modal order button
    const modalOrderBtn = document.getElementById('modalOrderBtn');
    if (modalOrderBtn) {
        modalOrderBtn.addEventListener('click', () => {
            closeDetailModal();
            agregarAlCarrito(id);
        });
    }
}

function closeDetailModal() {
    document.getElementById('detailModal').classList.remove('active');
}

// AJAX Cart Integration
function setupCartActions() {
    // Bind card action buttons (Detail & Pedir)
    document.querySelectorAll('.view-detail-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = parseInt(e.currentTarget.getAttribute('data-id'));
            openDetailModal(id);
        });
    });

    document.querySelectorAll('.order-trigger-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = parseInt(e.currentTarget.getAttribute('data-id'));
            agregarAlCarrito(id);
        });
    });
}

function agregarAlCarrito(tragoId) {
    fetch(`/carrito/agregar?tragoId=${tragoId}`, {
        method: 'POST'
    })
    .then(response => {
        // Si el servidor redirige (ej. hacia /login por no estar autenticado), seguimos la redirección
        if (response.redirected) {
            window.location.href = response.url;
            return;
        }
        return response.json();
    })
    .then(data => {
        if (!data) return;
        if (data.success) {
            // Actualizar Badge del Carrito
            const badge = document.getElementById('cartBadge');
            if (badge) {
                badge.innerText = data.cantidadTotal;
                badge.style.transform = 'scale(1.3)';
                setTimeout(() => badge.style.transform = 'scale(1)', 300);
            }
            showToast("¡Agregado al Carrito!", `El trago se añadió correctamente.`);
        } else if (data.redirect) {
            window.location.href = data.redirect;
        }
    })
    .catch(err => {
        console.error("Error al agregar al carrito:", err);
        // En caso de error de sesión, redirección fallback a login
        window.location.href = '/login';
    });
}

// Modal Listeners
function setupModalListeners() {
    // Detail Modal Close
    document.getElementById('modalCloseBtn')?.addEventListener('click', closeDetailModal);
    document.getElementById('detailModal')?.addEventListener('click', (e) => {
        if (e.target.id === 'detailModal') closeDetailModal();
    });

    // Quick feature button in hero (Ruby Rosemary Spritz - ID 1)
    document.getElementById('quickFeatureBtn')?.addEventListener('click', () => {
        openDetailModal(1);
    });

    // Cart Button Click: redirects to the checkout page
    document.getElementById('cartBtn')?.addEventListener('click', () => {
        window.location.href = '/carrito';
    });
}

// Toast Notification System
function showToast(title, message) {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `
        <i class="fa-solid fa-martini-glass-citrus toast-icon"></i>
        <div>
            <div class="toast-title">${title}</div>
            <div class="toast-msg">${message}</div>
        </div>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(50px)';
        setTimeout(() => toast.remove(), 400);
    }, 4500);
}

// Ambient Sparkling Bubbles Generator
function initBubbles() {
    const container = document.getElementById('bubblesContainer');
    if (!container) return;

    for (let i = 0; i < 20; i++) {
        const bubble = document.createElement('div');
        const size = Math.random() * 6 + 2;
        const left = Math.random() * 100;
        const duration = Math.random() * 10 + 6;
        const delay = Math.random() * 5;

        bubble.style.cssText = `
            position: fixed;
            bottom: -20px;
            left: ${left}%;
            width: ${size}px;
            height: ${size}px;
            background: rgba(255, 77, 109, ${Math.random() * 0.4 + 0.1});
            border-radius: 50%;
            pointer-events: none;
            z-index: 0;
            box-shadow: 0 0 8px rgba(255, 77, 109, 0.4);
            animation: floatUp ${duration}s linear ${delay}s infinite;
        `;
        container.appendChild(bubble);
    }

    if (!document.getElementById('bubbleStyle')) {
        const style = document.createElement('style');
        style.id = 'bubbleStyle';
        style.innerHTML = `
            @keyframes floatUp {
                0% { transform: translateY(0) scale(1); opacity: 0; }
                20% { opacity: 0.7; }
                80% { opacity: 0.7; }
                100% { transform: translateY(-105vh) scale(1.5); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
    }
}

// Mobile Menu Toggle
function setupMobileNav() {
    const toggle = document.getElementById('mobileToggle');
    const menu = document.getElementById('navMenu');

    toggle?.addEventListener('click', () => {
        menu?.classList.toggle('active');
    });

    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', () => {
            menu?.classList.remove('active');
        });
    });
}

// Header Scroll Effect
function setupHeaderScroll() {
    const header = document.getElementById('header');
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            header.style.padding = '0.8rem 2rem';
            header.style.boxShadow = '0 10px 30px rgba(0,0,0,0.8)';
        } else {
            header.style.padding = '1.2rem 2rem';
            header.style.boxShadow = 'none';
        }
    });
}
