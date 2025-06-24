let productService;

class ProductService {

    photos = [];
    page = 0;
    size = 12;
    lastCount = 0;


    filter = {
        cat: undefined,
        minPrice: undefined,
        maxPrice: undefined,
        color: undefined,
        q: undefined,
        queryString: () => {
            let qs = "";
            if (this.filter.cat) {
                qs = `cat=${this.filter.cat}`;
            }
            if (this.filter.minPrice) {
                const minP = `minPrice=${this.filter.minPrice}`;
                if (qs.length > 0) {
                    qs += `&${minP}`;
                } else {
                    qs = minP;
                }
            }
            if (this.filter.maxPrice) {
                const maxP = `maxPrice=${this.filter.maxPrice}`;
                if (qs.length > 0) {
                    qs += `&${maxP}`;
                } else {
                    qs = maxP;
                }
            }
            if (this.filter.color) {
                const col = `color=${this.filter.color}`;
                if (qs.length > 0) {
                    qs += `&${col}`;
                } else {
                    qs = col;
                }
            }
            if (this.filter.q) {
                const q = `q=${encodeURIComponent(this.filter.q)}`;
                if (qs.length > 0) {
                    qs += `&${q}`;
                } else {
                    qs = q;
                }
            }
            return qs.length > 0 ? `?${qs}` : "";
        }
    }

    constructor() {

        //load list of photos into memory
        axios.get("/images/products/photos.json")
            .then(response => {
                this.photos = response.data;
            });
    }

    hasPhoto(photo) {
        return this.photos.filter(p => p == photo).length > 0;
    }

    addCategoryFilter(cat) {
        if (cat == 0) this.clearCategoryFilter();
        else this.filter.cat = cat;
    }

    addMinPriceFilter(price) {
        if (price == 0 || price == "") this.clearMinPriceFilter();
        else this.filter.minPrice = price;
    }

    addMaxPriceFilter(price) {
        if (price == 0 || price == "") this.clearMaxPriceFilter();
        else this.filter.maxPrice = price;
    }

    addColorFilter(color) {
        if (color == "") this.clearColorFilter();
        else this.filter.color = color;
    }

    clearCategoryFilter() {
        this.filter.cat = undefined;
    }

    clearMinPriceFilter() {
        this.filter.minPrice = undefined;
    }

    clearMaxPriceFilter() {
        this.filter.maxPrice = undefined;
    }

    clearColorFilter() {
        this.filter.color = undefined;
    }

    clearAllFilters() {
        this.clearCategoryFilter();
        this.clearMinPriceFilter();
        this.clearMaxPriceFilter();
        this.clearColorFilter();
        this.clearSearchQuery();
        this.page = 0;
    }

    setSearchQuery(q) {
        if (q === "") this.clearSearchQuery();
        else this.filter.q = q;
    }

    clearSearchQuery() {
        this.filter.q = undefined;
    }

    search() {
        const url = `${config.baseUrl}/products?page=${this.page}&size=${this.size}${this.filter.queryString().replace('?', '&')}`;

        axios.get(url)
            .then(response => {
                let data = {};
                data.products = response.data;
                data.isAdmin = userService.getCurrentUser().role === 'ROLE_ADMIN';
                this.lastCount = data.products.length;

                data.products.forEach(product => {
                    if (product.imageUrl && product.imageUrl.startsWith('http')) {
                        product.imagePath = product.imageUrl;
                    } else if (this.hasPhoto(product.imageUrl)) {
                        product.imagePath = `/images/products/${product.imageUrl}`;
                    } else {
                        product.imagePath = '/images/products/no-image.jpg';
                    }
                })

                templateBuilder.build('product', data, 'content', () => {
                    this.enableButtons();
                    this.renderPagination();
                });

            })
            .catch(error => {

                const data = {
                    error: "Searching products failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    enableButtons() {
        const buttons = [...document.querySelectorAll(".add-button")];

        if (userService.isLoggedIn()) {
            buttons.forEach(button => {
                button.classList.remove("invisible")
            });
        } else {
            buttons.forEach(button => {
                button.classList.add("invisible")
            });
        }
    }

    renderPagination() {
        const ul = document.getElementById('pagination');
        if (!ul) return;
        ul.innerHTML = '';

        const prev = document.createElement('li');
        prev.className = 'page-item' + (this.page === 0 ? ' disabled' : '');
        prev.innerHTML = '<a class="page-link" href="#">Previous</a>';
        prev.addEventListener('click', (e) => {
            e.preventDefault();
            if (this.page > 0) {
                if (typeof playJumpSound === 'function') playJumpSound();
                this.page--;
                this.search();
            }
        });
        ul.appendChild(prev);

        const current = document.createElement('li');
        current.className = 'page-item active';
        current.innerHTML = `<a class="page-link" href="#">${this.page + 1}</a>`;
        ul.appendChild(current);

        if (this.lastCount === this.size) {
            const nextNum = document.createElement('li');
            nextNum.className = 'page-item';
            nextNum.innerHTML = `<a class="page-link" href="#">${this.page + 2}</a>`;
            nextNum.addEventListener('click', (e) => {
                e.preventDefault();
                this.page++;
                this.search();
            });
            ul.appendChild(nextNum);
        }

        const next = document.createElement('li');
        next.className = 'page-item' + (this.lastCount < this.size ? ' disabled' : '');
        next.innerHTML = '<a class="page-link" href="#">Next</a>';
        next.addEventListener('click', (e) => {
            e.preventDefault();
            if (this.lastCount === this.size) {
                if (typeof playJumpSound === 'function') playJumpSound();
                this.page++;
                this.search();
            }
        });
        ul.appendChild(next);
    }

    editProduct(id) {
        axios.all([
            axios.get(`${config.baseUrl}/products/${id}`),
            axios.get(`${config.baseUrl}/categories`)
        ]).then(axios.spread((prodRes, catRes) => {
            const data = prodRes.data;
            if (data.imageUrl && data.imageUrl.startsWith('http')) {
                data.imagePath = data.imageUrl;
            } else if (this.hasPhoto(data.imageUrl)) {
                data.imagePath = `/images/products/${data.imageUrl}`;
            } else {
                data.imagePath = '/images/products/no-image.jpg';
            }
            data.categories = catRes.data.map(c => ({
                ...c,
                selected: c.categoryId === data.categoryId
            }));
            templateBuilder.build('edit-product-offcanvas', data, 'editProductSidebarBody', () => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('editProductSidebar'));
                off.show();
            });
        }));
    }

    saveProduct(id) {
        const product = {
            name: document.getElementById('edit-name').value,
            price: parseFloat(document.getElementById('edit-price').value),
            categoryId: parseInt(document.getElementById('edit-category').value),
            description: document.getElementById('edit-description').value,
            color: document.getElementById('edit-color').value,
            stock: parseInt(document.getElementById('edit-stock').value),
            featured: document.getElementById('edit-featured').checked,
            imageUrl: document.getElementById('edit-image').value
        };
        axios.put(`${config.baseUrl}/products/${id}`, product)
            .then(() => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('editProductSidebar'));
                off.hide();
                this.search();
            });
    }

    confirmDelete(id) {
        axios.get(`${config.baseUrl}/products/${id}`)
            .then(res => {
                const data = res.data;
                if (data.imageUrl && data.imageUrl.startsWith('http')) {
                    data.imagePath = data.imageUrl;
                } else if (this.hasPhoto(data.imageUrl)) {
                    data.imagePath = `/images/products/${data.imageUrl}`;
                } else {
                    data.imagePath = '/images/products/no-image.jpg';
                }
                templateBuilder.build('confirm-delete-product-offcanvas', data, 'deleteProductSidebarBody', () => {
                    const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('deleteProductSidebar'));
                    off.show();
                });
            });
    }

    deleteProduct(id) {
        axios.delete(`${config.baseUrl}/products/${id}`)
            .then(() => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('deleteProductSidebar'));
                if (typeof disableNextNavSound === 'function') {
                    disableNextNavSound();
                }
                off.hide();
                this.search();
                playFireballSound();
            });
    }

    showAddProduct() {
        categoryService.getAllCategories(categories => {
            templateBuilder.build('add-product-offcanvas', {categories}, 'addProductSidebarBody', () => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('addProductSidebar'));
                off.show();
            });
        });
    }

    createProduct() {
        const product = {
            name: document.getElementById('add-name').value,
            price: parseFloat(document.getElementById('add-price').value),
            categoryId: parseInt(document.getElementById('add-category').value),
            description: document.getElementById('add-description').value,
            color: document.getElementById('add-color').value,
            stock: parseInt(document.getElementById('add-stock').value),
            featured: document.getElementById('add-featured').checked,
            imageUrl: document.getElementById('add-image').value
        };
        axios.post(`${config.baseUrl}/products`, product)
            .then(() => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('addProductSidebar'));
                off.hide();
                this.search();
            });
    }

}


document.addEventListener('DOMContentLoaded', () => {
    productService = new ProductService();

});
