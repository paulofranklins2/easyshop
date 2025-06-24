let wishlistService;

class WishlistService {
    items = [];

    updateBadge() {
        try {
            const badge = document.getElementById('wishlist-items');
            if (badge) badge.innerText = this.items.length;
        } catch (e) {
        }
    }

    getImagePath(url) {
        if (url.startsWith('http')) return url;
        if (productService && productService.hasPhoto(url)) {
            return `/images/products/${url}`;
        }
        return '/images/products/no-image.jpg';
    }

    loadWishlist() {
        axios.get(`${config.baseUrl}/wishlist`)
            .then(res => {
                this.items = res.data;
                this.updateDisplay();
            });
    }

    addToWishlist(productId) {
        axios.post(`${config.baseUrl}/wishlist/${productId}`, {})
            .then(res => {
                this.items = res.data;
                this.updateDisplay();
                if (typeof playJumpSound() === 'function') {
                    playJumpSound();
                }
            });
    }

    remove(productId) {
        axios.delete(`${config.baseUrl}/wishlist/${productId}`)
            .then(res => {
                this.items = res.data;
                this.updateDisplay();
                if (typeof playFireballSound === 'function') {
                    playFireballSound();
                }
            });
    }

    moveToCart(productId) {
        axios.post(`${config.baseUrl}/wishlist/${productId}/cart`, {})
            .then(() => {
                this.loadWishlist();
                cartService.loadCart();
                const off = bootstrap.Offcanvas.getInstance(document.getElementById('wishlistSidebar'));
                if (off) off.show();
                if (typeof playCheckoutSound === 'function') {
                    playCheckoutSound();
                }
            });
    }

    updateDisplay() {
        const container = document.getElementById('wishlistSidebarItems');
        if (!container) return;
        container.innerHTML = '';
        this.items.forEach(p => {
            const div = document.createElement('div');
            div.className = 'd-flex justify-content-between align-items-center mb-3';
            const imgPath = this.getImagePath(p.imageUrl);
            div.innerHTML = `<div class="d-flex align-items-center"><img src="${imgPath}" style="width:40px;height:40px;object-fit:contain" class="me-2"><div><strong>${p.name}</strong><br>$${p.price}</div></div>`;

            const btnGroup = document.createElement('div');

            const cartBtn = document.createElement('button');
            cartBtn.className = 'btn btn-success btn-sm me-2'; // green
            cartBtn.innerText = '🛒';
            cartBtn.addEventListener('click', () => this.moveToCart(p.productId));

            const delBtn = document.createElement('button');
            delBtn.className = 'btn btn-danger btn-sm'; // red
            delBtn.innerText = '❌';
            delBtn.addEventListener('click', () => this.remove(p.productId));

            btnGroup.appendChild(cartBtn);
            btnGroup.appendChild(delBtn);
            div.appendChild(btnGroup);
            container.appendChild(div);
        });
        this.updateBadge();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    wishlistService = new WishlistService();
    if (userService.isLoggedIn()) wishlistService.loadWishlist();
});
