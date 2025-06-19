let cartService;

class ShoppingCartService {

    cart = {
        items: [],
        total: 0
    };

    addToCart(productId) {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        // const headers = userService.getHeaders();

        axios.post(url, {})// ,{headers})
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()
                this.renderSidebar()
            })
            .catch(error => {

                const data = {
                    error: "Add to cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    setCart(data) {
        this.cart = {
            items: [],
            total: 0
        }

        this.cart.total = data.total;

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }
    }

    loadCart() {

        const url = `${config.baseUrl}/cart`;

        axios.get(url)
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()
                this.renderSidebar()
            })
            .catch(error => {

                const data = {
                    error: "Load cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })

    }

    loadCartPage() {
        // templateBuilder.build("cart", this.cart, "main");

        const main = document.getElementById("main")
        main.innerHTML = "";

        let div = document.createElement("div");
        div.classList = "filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div")
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const cartHeader = document.createElement("div")
        cartHeader.classList.add("cart-header")

        const h1 = document.createElement("h1")
        h1.innerText = "Cart";
        cartHeader.appendChild(h1);

        const button = document.createElement("button");
        button.classList.add("btn")
        button.classList.add("btn-danger")
        button.innerText = "Clear";
        button.addEventListener("click", () => this.clearCart());
        cartHeader.appendChild(button)

        contentDiv.appendChild(cartHeader)
        main.appendChild(contentDiv);

        // let parent = document.getElementById("cart-item-list");
        this.cart.items.forEach(item => {
            this.buildItem(item, contentDiv)
        });
    }

    renderSidebar() {
        const container = document.getElementById('cartSidebarItems');
        if (!container) return;
        container.innerHTML = '';
        this.cart.items.forEach(item => {
            const max = item.product.stock;
            const options = Array.from({length: max}, (_, i) => `<option ${i + 1 === item.quantity ? 'selected' : ''}>${i + 1}</option>`).join('');
            const div = document.createElement('div');
            div.className = 'd-flex justify-content-between align-items-center mb-3';
            div.innerHTML = `<div class="d-flex align-items-center"><img src="/images/products/${item.product.imageUrl}" style="width:40px;height:40px;object-fit:contain" class="me-2"><div><strong>${item.product.name}</strong><br>$${item.product.price} × <select class="form-select form-select-sm d-inline-block w-auto cartQty" data-id="${item.product.productId}">${options}</select></div></div><button class="btn btn-danger btn-sm remove-item" data-id="${item.product.productId}">Remove</button>`;
            container.appendChild(div);
        });
        const totalDiv = document.getElementById('cartSidebarTotal');
        if (totalDiv) totalDiv.innerText = `Total: $${this.cart.total.toFixed(2)}`;
        container.querySelectorAll('.cartQty').forEach(sel => {
            sel.addEventListener('change', (e) => {
                this.updateQuantity(sel.dataset.id, parseInt(sel.value));
            });
        });
        container.querySelectorAll('.remove-item').forEach(btn => {
            btn.addEventListener('click', () => {
                this.deleteProduct(btn.dataset.id);
            });
        });
    }

    updateQuantity(productId, qty) {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        axios.put(url, {quantity: qty})
            .then(res => {
                this.setCart(res.data);
                this.updateCartDisplay();
                this.renderSidebar();
            });
    }

    deleteProduct(productId) {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        axios.delete(url)
            .then(res => {
                this.setCart(res.data);
                this.updateCartDisplay();
                this.renderSidebar();
            });
    }

    buildItem(item, parent) {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        let div = document.createElement("div");
        outerDiv.appendChild(div);
        let h4 = document.createElement("h4")
        h4.innerText = item.product.name;
        div.appendChild(h4);

        let photoDiv = document.createElement("div");
        photoDiv.classList.add("photo")
        let img = document.createElement("img");
        img.src = `/images/products/${item.product.imageUrl}`
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src)
        })
        photoDiv.appendChild(img)
        let priceH4 = document.createElement("h4");
        priceH4.classList.add("price");
        priceH4.innerText = `$${item.product.price}`;
        photoDiv.appendChild(priceH4);
        outerDiv.appendChild(photoDiv);

        let descriptionDiv = document.createElement("div");
        descriptionDiv.innerText = item.product.description;
        outerDiv.appendChild(descriptionDiv);

        let quantityDiv = document.createElement("div")
        quantityDiv.innerText = `Quantity: ${item.quantity}`;
        outerDiv.appendChild(quantityDiv)


        parent.appendChild(outerDiv);
    }

    clearCart() {

        const url = `${config.baseUrl}/cart`;

        axios.delete(url)
            .then(response => {
                this.cart = {
                    items: [],
                    total: 0
                }

                this.cart.total = response.data.total;

                for (const [key, value] of Object.entries(response.data.items)) {
                    this.cart.items.push(value);
                }

                this.updateCartDisplay()
                this.renderSidebar()

            })
            .catch(error => {

                const data = {
                    error: "Empty cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    updateCartDisplay() {
        try {
            const itemCount = this.cart.items.length;
            const cartControl = document.getElementById("cart-items")

            cartControl.innerText = itemCount;
        } catch (e) {

        }
    }
}


document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();

    if (userService.isLoggedIn()) {
        cartService.loadCart();
    }

});
