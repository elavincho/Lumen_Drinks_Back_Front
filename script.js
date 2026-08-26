/**
 * LUMEN | Artisanal Mixology Interactive Engine
 * Handles dynamic cocktail rendering, filtering, detail modal,
 * order simulation modal, toasts, and background bubble animations.
 */

// 1. Cocktails Database (10 Distinct Artisanal Drinks)
const COCKTAILS = [
    {
        id: 1,
        name: "Ruby Rosemary Spritz",
        category: "insignia",
        price: 12.50,
        badge: "Insignia de la Casa",
        image: "images/ruby_spritz.png",
        shortDesc: "Gin boutique, jugo fresco de pomelo rubí, romero flameado y tónica cristalina.",
        fullDesc: "Nuestro cóctel estrella insuflado con los aromas de la barra. Utiliza pomelos rubí seleccionados a mano, macerados en frío con romero silvestre flameado al momento para liberar sus aceites esenciales.",
        ingredients: ["Gin Boutique", "Jugo de Pomelo Rubí", "Licor de Flor de Saúco", "Romero Flameado", "Tónica Premium", "Hielo Cristal"],
        profile: { citric: 95, herbal: 85, sweet: 40, alcohol: "14%" }
    },
    {
        id: 2,
        name: "Smoked Citrus Bourbon",
        category: "ahumados",
        price: 14.00,
        badge: "Ahumado a la Madera",
        image: "images/smoked_bourbon.png",
        shortDesc: "Bourbon añejo 8 años, bitters de naranja, sirope de romero y humo de virutas de roble.",
        fullDesc: "Una experiencia ahumada inmersiva. Servido en vaso rock con hielo de fusión lenta y infusionado bajo campana con humo aromático de madera de roble tostado.",
        ingredients: ["Bourbon Añejo 8 Años", "Bitters de Naranja", "Sirope de Romero", "Piel de Pomelo Flameada", "Humo de Roble"],
        profile: { citric: 60, herbal: 70, sweet: 35, alcohol: "22%" }
    },
    {
        id: 3,
        name: "Midnight Berry Fizz",
        category: "citricos",
        price: 11.50,
        badge: "Frutal & Efervescente",
        image: "images/berry_fizz.png",
        shortDesc: "Vodka ultra-premium, macerado de frutos rojos, limón eureka y soda de flor de hibisco.",
        fullDesc: "Burbujas de tono violeta profundo y sabor vibrante. La dulzura natural de las moras silvestres se equilibra con la acidez refrescante del limón y el hibisco.",
        ingredients: ["Vodka Ultra-Premium", "Macerado de Moras", "Licor de Cassis", "Jugo de Limón Eureka", "Soda de Hibisco"],
        profile: { citric: 75, herbal: 30, sweet: 65, alcohol: "12%" }
    },
    {
        id: 4,
        name: "Crimson Grapefruit Paloma",
        category: "citricos",
        price: 13.00,
        badge: "Cítrico Imperial",
        image: "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?auto=format&fit=crop&w=800&q=80",
        shortDesc: "Mezcal silvestre 100% agave, jugo de pomelo rosado, cordial de lima y sal volcánica.",
        fullDesc: "Redefinición mexicana del clásico Paloma: notas de agaves ahumados ensambladas con la acidez limpia del pomelo rosa y escarchado de sal negra de romero.",
        ingredients: ["Mezcal Silvestre 100% Agave", "Jugo de Pomelo Rubí", "Cordial de Lima", "Sal Volcánica de Romero", "Soda Cristal"],
        profile: { citric: 90, herbal: 65, sweet: 40, alcohol: "16%" }
    },
    {
        id: 5,
        name: "Botanical Velvet Tonic",
        category: "insignia",
        price: 11.00,
        badge: "Herbal Elegante",
        image: "https://images.unsplash.com/photo-1551024709-8f23befc6f87?auto=format&fit=crop&w=800&q=80",
        shortDesc: "Gin infusionado en pepino y romero, licor de saúco, tónica artesanal y bayas de enebro.",
        fullDesc: "Trago equilibrado de perfil floral y textura sedosa. La infusión botánica resalta la frescura del enebro y la rodaja de pomelo rubí.",
        ingredients: ["Gin Infusionado en Pepino", "Licor de Saúco", "Tónica Artesanal", "Bayas de Enebro", "Pomelo Rubí"],
        profile: { citric: 65, herbal: 90, sweet: 45, alcohol: "13%" }
    },
    {
        id: 6,
        name: "Emerald Mint Julep",
        category: "ahumados",
        price: 12.00,
        badge: "Refrescante",
        image: "https://images.unsplash.com/photo-1556881286-fc6915169721?auto=format&fit=crop&w=800&q=80",
        shortDesc: "Whiskey de centeno, menta fresca del huerto, azúcar virgen y perfume de romero tostado.",
        fullDesc: "Clásico sureño renovado con menta recién molienda, hielo triturado de lenta fusión y perfume ahumado de romero tostado en copa de plata.",
        ingredients: ["Whiskey de Centeno", "Menta Fresca de Huerto", "Azúcar de Caña Virgen", "Hielo Frappé", "Perfume de Romero"],
        profile: { citric: 40, herbal: 95, sweet: 60, alcohol: "18%" }
    },
    {
        id: 7,
        name: "Golden Passion Negroni",
        category: "ahumados",
        price: 14.50,
        badge: "Amargo de Autor",
        image: "https://images.unsplash.com/photo-1560512823-829485b8bf24?auto=format&fit=crop&w=800&q=80",
        shortDesc: "Gin London Dry, Campari italiano, Vermut rojo con maracuyá y aceites de pomelo.",
        fullDesc: "Giro exótico e intrigante al Negroni tradicional. Equilibra el amargor característico del Campari con notas tropicales de maracuyá y aceites cítricos.",
        ingredients: ["Gin London Dry", "Campari Italiano", "Vermut Rojo con Maracuyá", "Aceites de Pomelo Rubí"],
        profile: { citric: 65, herbal: 50, sweet: 35, alcohol: "24%" }
    },
    {
        id: 8,
        name: "Sparkling Rosemary Mocktail",
        category: "sin-alcohol",
        price: 8.50,
        badge: "Sin Alcohol 0.0%",
        image: "https://images.unsplash.com/photo-1621263764928-df1444c5e859?auto=format&fit=crop&w=800&q=80",
        shortDesc: "Cordial de pomelo rubí, romero fresco, jugo de lima, manzanilla y soda carbonatada.",
        fullDesc: "Cóctel botánico sin alcohol diseñado con la misma sofisticación. Todo el perfil aromático de cítricos y hierbas sin graduación alcohólica.",
        ingredients: ["Cordial de Pomelo Rubí", "Rama de Romero Fresco", "Jugo de Lima Eureka", "Extracto de Manzanilla", "Soda Artesanal"],
        profile: { citric: 90, herbal: 80, sweet: 45, alcohol: "0.0%" }
    },
    {
        id: 9,
        name: "Tropical Coral Dragon",
        category: "citricos",
        price: 13.50,
        badge: "Exótico Coral",
        image: "https://images.unsplash.com/photo-1536935338788-846bb9981813?auto=format&fit=crop&w=800&q=80",
        shortDesc: "Ron blanco de coco, pitahaya roja, jugo de pomelo rubí, triple sec y bitters de romero.",
        fullDesc: "Un trago visualmente deslumbrante de tono coral intenso. Fusiona frutas exóticas tropicales con la acidez distintiva de nuestra barra.",
        ingredients: ["Ron Blanco de Coco", "Reducción de Pitahaya Roja", "Jugo de Pomelo Rubí", "Triple Sec", "Bitters de Romero"],
        profile: { citric: 75, herbal: 40, sweet: 70, alcohol: "15%" }
    },
    {
        id: 10,
        name: "Aromatic Mezcalita Rosada",
        category: "insignia",
        price: 13.50,
        badge: "Especialidad Mezcal",
        image: "https://images.unsplash.com/photo-1572116469696-31de0f17cc34?auto=format&fit=crop&w=800&q=80",
        shortDesc: "Mezcal joven ahumado, pomelo rubí, licor Ancho Reyes, jarabe de agave y sal de chile.",
        fullDesc: "Carácter ahumado profundo combinado con el dulzor orgánico del agave y el picante suave del chile en el borde del vaso.",
        ingredients: ["Mezcal Joven Ahumado", "Extracto de Pomelo Rubí", "Licor Ancho Reyes", "Jarabe de Agave", "Sal de Romero & Chile"],
        profile: { citric: 80, herbal: 60, sweet: 50, alcohol: "17%" }
    }
];

// State Management
let cartCount = 0;
let activeCategory = 'all';

// DOM Elements Initialization
document.addEventListener('DOMContentLoaded', () => {
    initBubbles();
    renderCocktails(COCKTAILS);
    setupFilterListeners();
    setupModalListeners();
    setupMobileNav();
    setupHeaderScroll();
});

// Render Cocktails Grid
function renderCocktails(items) {
    const grid = document.getElementById('cocktailsGrid');
    if (!grid) return;

    if (items.length === 0) {
        grid.innerHTML = `<p style="grid-column: 1/-1; text-align: center; color: var(--text-secondary); padding: 3rem;">No se encontraron tragos en esta categoría.</p>`;
        return;
    }

    grid.innerHTML = items.map(drink => `
        <article class="cocktail-card" data-id="${drink.id}">
            <div class="cocktail-img-container">
                <img src="${drink.image}" alt="${drink.name}" class="cocktail-img" loading="lazy" onerror="this.src='images/ruby_spritz.png'">
                <span class="cocktail-badge"><i class="fa-solid fa-sparkles"></i> ${drink.badge}</span>
            </div>
            <div class="cocktail-info">
                <div class="cocktail-header">
                    <h3 class="cocktail-title">${drink.name}</h3>
                    <span class="cocktail-price">$${drink.price.toFixed(2)}</span>
                </div>
                <p class="cocktail-desc">${drink.shortDesc}</p>
                
                <span class="ingredients-title"><i class="fa-solid fa-leaf"></i> Ingredientes</span>
                <div class="ingredients-list">
                    ${drink.ingredients.map(ing => `<span class="ingredient-pill">${ing}</span>`).join('')}
                </div>
                
                <div class="card-actions">
                    <button class="btn-detail view-detail-btn" data-id="${drink.id}">
                        <i class="fa-solid fa-circle-info"></i> Detalle
                    </button>
                    <button class="btn btn-primary btn-sm btn-order-card order-trigger-btn" data-id="${drink.id}">
                        <i class="fa-solid fa-cart-plus"></i> Pedir
                    </button>
                </div>
            </div>
        </article>
    `).join('');

    // Rebind action buttons inside cards
    bindCardEvents();
}

// Event Bindings for Cards
function bindCardEvents() {
    document.querySelectorAll('.view-detail-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = parseInt(e.currentTarget.getAttribute('data-id'));
            openDetailModal(id);
        });
    });

    document.querySelectorAll('.order-trigger-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = parseInt(e.currentTarget.getAttribute('data-id'));
            triggerOrderProcess(id);
        });
    });
}

// Category Filter Handlers
function setupFilterListeners() {
    const filterBtns = document.querySelectorAll('.filter-btn');
    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            filterBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const category = btn.getAttribute('data-category');
            activeCategory = category;

            if (category === 'all') {
                renderCocktails(COCKTAILS);
            } else {
                const filtered = COCKTAILS.filter(c => c.category === category);
                renderCocktails(filtered);
            }
        });
    });
}

// Detail Modal Handler
function openDetailModal(id) {
    const drink = COCKTAILS.find(c => c.id === id);
    if (!drink) return;

    const modalBody = document.getElementById('modalBody');
    modalBody.innerHTML = `
        <div style="padding: 1rem 0;">
            <div style="width: 100%; height: 260px; border-radius: var(--radius-md); overflow: hidden; margin-bottom: 1.5rem; background: #000;">
                <img src="${drink.image}" alt="${drink.name}" style="width: 100%; height: 100%; object-fit: cover;" onerror="this.src='images/ruby_spritz.png'">
            </div>
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.8rem;">
                <h2 style="font-size: 1.8rem;">${drink.name}</h2>
                <span style="font-family: var(--font-heading); font-size: 1.8rem; color: var(--citrus-gold); font-weight: 800;">$${drink.price.toFixed(2)}</span>
            </div>

            <p style="color: var(--text-secondary); margin-bottom: 1.5rem; line-height: 1.6;">${drink.fullDesc}</p>

            <h4 style="font-size: 0.9rem; text-transform: uppercase; color: var(--rosemary-green-light); letter-spacing: 1px; margin-bottom: 0.8rem;">
                <i class="fa-solid fa-list-check"></i> Ingredientes Completos
            </h4>
            <ul style="list-style: none; display: grid; grid-template-columns: 1fr 1fr; gap: 0.6rem; margin-bottom: 1.8rem;">
                ${drink.ingredients.map(ing => `
                    <li style="font-size: 0.9rem; background: rgba(255,255,255,0.05); padding: 0.5rem 0.8rem; border-radius: 8px; border: 1px solid var(--border-glass);">
                        <i class="fa-solid fa-check" style="color: var(--ruby-pink); margin-right: 6px;"></i> ${ing}
                    </li>
                `).join('')}
            </ul>

            <div style="background: rgba(255,255,255,0.03); padding: 1.2rem; border-radius: var(--radius-md); border: 1px solid var(--border-glass); margin-bottom: 1.8rem;">
                <h4 style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 0.8rem;">PERFIL DE SABOR & GRADUACIÓN</h4>
                <div style="display: flex; justify-content: space-between; font-size: 0.85rem; color: var(--text-secondary);">
                    <span>Cítrico: <strong>${drink.profile.citric}%</strong></span>
                    <span>Herbal: <strong>${drink.profile.herbal}%</strong></span>
                    <span>Dulzura: <strong>${drink.profile.sweet}%</strong></span>
                    <span>Alcohol: <strong>${drink.profile.alcohol}</strong></span>
                </div>
            </div>

            <button class="btn btn-primary btn-block btn-lg order-trigger-btn" data-id="${drink.id}" id="modalOrderBtn">
                <i class="fa-solid fa-martini-glass"></i> Confirmar y Pedir $${drink.price.toFixed(2)}
            </button>
        </div>
    `;

    document.getElementById('detailModal').classList.add('active');

    // Bind modal order button
    const modalOrderBtn = document.getElementById('modalOrderBtn');
    if (modalOrderBtn) {
        modalOrderBtn.addEventListener('click', () => {
            closeDetailModal();
            triggerOrderProcess(drink.id);
        });
    }
}

function closeDetailModal() {
    document.getElementById('detailModal').classList.remove('active');
}

// SIMULATED ORDER PROCESS HANDLER (Exigido por la consigna)
function triggerOrderProcess(id) {
    const drink = COCKTAILS.find(c => c.id === id);
    const drinkName = drink ? drink.name : "Ruby Rosemary Spritz";
    const ingredients = drink ? drink.ingredients.slice(0, 3).join(', ') : "Pomelo, Romero, Gin";

    // Update Cart Badge
    cartCount++;
    const badge = document.getElementById('cartBadge');
    if (badge) {
        badge.innerText = cartCount;
        badge.style.transform = 'scale(1.3)';
        setTimeout(() => badge.style.transform = 'scale(1)', 300);
    }

    // Setup Order Modal Info
    document.getElementById('orderDrinkName').innerText = `Preparando tu "${drinkName}"...`;
    document.getElementById('orderIngredientsSummary').innerText = ingredients;
    document.getElementById('orderNumber').innerText = `#LUMEN-${Math.floor(1000 + Math.random() * 9000)}`;

    // Reset Progress Bar Animation
    const progressBar = document.getElementById('orderProgressBar');
    if (progressBar) {
        progressBar.style.width = '10%';
        setTimeout(() => progressBar.style.width = '65%', 400);
        setTimeout(() => progressBar.style.width = '90%', 2500);
    }

    // Show Simulated Order Modal
    const orderModal = document.getElementById('orderProcessModal');
    orderModal.classList.add('active');

    // Trigger Toast Notification Feedback
    showToast("¡Pedido en proceso!", `Tu ${drinkName} ya se está preparando en la barra principal.`);
}

function closeOrderModal() {
    document.getElementById('orderProcessModal').classList.remove('active');
}

// Modal Listeners
function setupModalListeners() {
    // Detail Modal Close
    document.getElementById('modalCloseBtn')?.addEventListener('click', closeDetailModal);
    document.getElementById('detailModal')?.addEventListener('click', (e) => {
        if (e.target.id === 'detailModal') closeDetailModal();
    });

    // Order Modal Close
    document.getElementById('orderModalCloseBtn')?.addEventListener('click', closeOrderModal);
    document.getElementById('confirmOrderSuccessBtn')?.addEventListener('click', closeOrderModal);
    document.getElementById('orderProcessModal')?.addEventListener('click', (e) => {
        if (e.target.id === 'orderProcessModal') closeOrderModal();
    });

    // Quick feature button in hero
    document.getElementById('quickFeatureBtn')?.addEventListener('click', () => {
        openDetailModal(1); // Ruby Rosemary Spritz
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

    // Add Keyframe dynamically if needed
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
