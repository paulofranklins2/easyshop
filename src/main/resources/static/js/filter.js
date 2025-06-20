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
}