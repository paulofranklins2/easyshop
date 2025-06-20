function showLoginForm() {
    templateBuilder.build('login-form', {}, 'login');
}

function showRegisterForm() {
    templateBuilder.build('register-form', {}, 'login');
}


function hideModalForm() {
    templateBuilder.clear('login');
}

function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    userService.login(username, password);
    hideModalForm()
}

function register() {
    const username = document.getElementById('reg-username').value;
    const password = document.getElementById('reg-password').value;
    const confirm = document.getElementById('reg-confirm').value;
    if (password !== confirm) return;
    userService.register(username, password, confirm);
    hideModalForm();
}


function showImageDetailForm(product, imageUrl) {
    const imageDetail = {
        name: product,
        imageUrl: imageUrl
    };

    templateBuilder.build('image-detail', imageDetail, 'login')
}

function loadHome() {
    const main = document.getElementById('main');
    main.classList.remove('with-sidebar');
    templateBuilder.build('home', {}, 'main');

    productService.search();
}

function editProfile() {
    profileService.loadProfile();
}

function saveProfile() {
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const phone = document.getElementById("phone").value;
    const email = document.getElementById("email").value;
    const address = document.getElementById("address").value;
    const city = document.getElementById("city").value;
    const state = document.getElementById("state").value;
    const zip = document.getElementById("zip").value;
    const photoUrl = document.getElementById("photoUrl").value;
    const profileUrl = document.getElementById("profileUrl").value;

    const profile = {
        firstName,
        lastName,
        phone,
        email,
        address,
        city,
        state,
        zip,
        photoUrl,
        profileUrl
    };

    profileService.updateProfile(profile);
}

function showCart() {
    cartService.loadCart();
    const offcanvas = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('cartSidebar'));
    offcanvas.show();
}

function showFilters() {
    templateBuilder.build('filter', {}, 'filterSidebarBody', () => {
        categoryService.getAllCategories(loadCategories);
        restoreFilters();
        const offcanvas = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('filterSidebar'));
        offcanvas.show();
    });
}

function applyFilters(event) {
    if (event) event.preventDefault();
    const q = document.getElementById('search-input').value;
    const cat = document.getElementById('category-select').value;
    const color = document.getElementById('color-select').value;
    const min = document.getElementById('min-price').value;
    const max = document.getElementById('max-price').value;

    productService.setSearchQuery(q);
    productService.addCategoryFilter(cat);
    productService.addColorFilter(color);
    productService.addMinPriceFilter(min);
    productService.addMaxPriceFilter(max);

    productService.search();
    const offcanvas = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('filterSidebar'));
    offcanvas.hide();
}


function clearCart() {
    cartService.clearCart();
    cartService.loadCartPage();
}

function setCategory(control) {
    productService.addCategoryFilter(control.value);
    productService.search();

}

function setColor(control) {
    productService.addColorFilter(control.value);
    productService.search();

}

function setMinPrice(control) {
    const label = document.getElementById("min-price-display")
    label.innerText = control.value;

    const value = control.value != 0 ? control.value : "";
    productService.addMinPriceFilter(value)
    productService.search();

}

function setMaxPrice(control) {
    const label = document.getElementById("max-price-display")
    label.innerText = control.value;

    const value = control.value != 1500 ? control.value : "";
    productService.addMaxPriceFilter(value)
    productService.search();

}

document.addEventListener('DOMContentLoaded', () => {

    loadHome();
});
