let wishlistService;

class WishlistService {
    items = [];

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
            });
    }

    remove(productId) {
        axios.delete(`${config.baseUrl}/wishlist/${productId}`)
            .then(res => {
                this.items = res.data;
                this.updateDisplay();
            });
    }

    moveToCart(productId) {
        axios.post(`${config.baseUrl}/wishlist/${productId}/cart`, {})
            .then(() => {
                this.loadWishlist();
                cartService.loadCart();
                const off = bootstrap.Offcanvas.getInstance(document.getElementById('wishlistSidebar'));
                if (off) off.show();
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
            div.innerHTML = `<div class="d-flex align-items-center"><img src="${imgPath}" style="width:40px;height:40px;object-fit:contain" class="me-2"><strong>${p.name}</strong></div>`;
            const btnGroup = document.createElement('div');
            const cartBtn = document.createElement('button');
            cartBtn.className = 'btn btn-primary btn-sm me-2';
            cartBtn.innerText = 'Add to Cart';
            cartBtn.addEventListener('click', () => this.moveToCart(p.productId));
            const delBtn = document.createElement('button');
            delBtn.className = 'btn btn-danger btn-sm';
            delBtn.innerText = 'Remove';
            delBtn.addEventListener('click', () => this.remove(p.productId));
            btnGroup.appendChild(cartBtn);
            btnGroup.appendChild(delBtn);
            div.appendChild(btnGroup);
            container.appendChild(div);
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    wishlistService = new WishlistService();
    if (userService.isLoggedIn()) wishlistService.loadWishlist();
});
