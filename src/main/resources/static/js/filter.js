function loadCategories(categories) {
    const select = document.getElementById('category-select');
    if (!select) return;
    while (select.options.length > 1) {
        select.remove(1);
    }
    categories.forEach(c => {
        const option = document.createElement('option');
        option.setAttribute('value', c.categoryId);
        option.innerText = c.name;
        select.appendChild(option);
    })

    // restore selected category if one is set
    if (productService && productService.filter.cat) {
        select.value = productService.filter.cat;
    }
}

function loadColors(colors) {
    const select = document.getElementById('color-select');
    if (!select) return;
    while (select.options.length > 1) {
        select.remove(1);
    }
    colors.forEach(c => {
        const option = document.createElement('option');
        option.innerText = c;
        select.appendChild(option);
    });

    if (productService && productService.filter.color) {
        select.value = productService.filter.color;
    }
}

function restoreFilters() {
    if (!productService) return;
    const filter = productService.filter;

    const searchInput = document.getElementById('search-input');
    if (searchInput) searchInput.value = filter.q || '';

    const minPrice = document.getElementById('min-price');
    const minDisplay = document.getElementById('min-price-display');
    if (minPrice) {
        const value = filter.minPrice ?? 0;
        minPrice.value = value;
        if (minDisplay) minDisplay.innerText = value;
    }

    const maxPrice = document.getElementById('max-price');
    const maxDisplay = document.getElementById('max-price-display');
    if (maxPrice) {
        const value = filter.maxPrice ?? 1500;
        maxPrice.value = value;
        if (maxDisplay) maxDisplay.innerText = value;
    }

    const colorSelect = document.getElementById('color-select');
    if (colorSelect) colorSelect.value = filter.color || '';
}

function clearFilterControls() {
    const searchInput = document.getElementById('search-input');
    if (searchInput) searchInput.value = '';

    const minPrice = document.getElementById('min-price');
    const minDisplay = document.getElementById('min-price-display');
    if (minPrice) {
        minPrice.value = 0;
        if (minDisplay) minDisplay.innerText = 0;
    }

    const maxPrice = document.getElementById('max-price');
    const maxDisplay = document.getElementById('max-price-display');
    if (maxPrice) {
        maxPrice.value = 1500;
        if (maxDisplay) maxDisplay.innerText = 1500;
    }

    const colorSelect = document.getElementById('color-select');
    if (colorSelect) colorSelect.value = '';

    const categorySelect = document.getElementById('category-select');
    if (categorySelect) categorySelect.value = 0;
}
