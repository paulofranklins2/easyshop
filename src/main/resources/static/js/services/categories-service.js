let categoryService;

class CategoryService {


    getAllCategories(callback) {
        const url = `${config.baseUrl}/categories`;

        return axios.get(url)
            .then(response => {
                callback(response.data);
            })
            .catch(error => {

                const data = {
                    error: "Loading categories failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    showCategoryManager() {
        this.getAllCategories(cats => {
            templateBuilder.build('categories-offcanvas', {categories: cats}, 'categorySidebarBody', () => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('categorySidebar'));
                off.show();
            });
        });
    }

    showAddCategoryModal() {
        templateBuilder.build('add-category-modal', {}, 'login', () => {
            const modal = document.getElementById('add-category-modal');
            if (modal) modal.style.display = 'flex';
        });
    }

    createCategory() {
        const category = {
            name: document.getElementById('new-category-name').value,
            description: document.getElementById('new-category-description').value
        };
        axios.post(`${config.baseUrl}/categories`, category)
            .then(() => {
                hideModalForm();
                this.showCategoryManager();
            });
    }

    showEditCategoryModal(id, name, description) {
        templateBuilder.build('edit-category-modal', {categoryId: id, name, description}, 'login', () => {
            const modal = document.getElementById('edit-category-modal');
            if (modal) modal.style.display = 'flex';
        });
    }

    updateCategory(id) {
        const category = {
            name: document.getElementById('edit-category-name').value,
            description: document.getElementById('edit-category-description').value
        };
        axios.put(`${config.baseUrl}/categorids/${id}`, category)
            .then(() => {
                hideModalForm();
                this.showCategoryManager();
            });
    }

    showDeleteCategoryModal(id) {
        templateBuilder.build('confirm-delete-category', {categoryId: id}, 'login', () => {
            const modal = document.getElementById('delete-category-modal');
            if (modal) modal.style.display = 'flex';
        });
    }

    deleteCategory(id) {
        axios.delete(`${config.baseUrl}/categorids/${id}`)
            .then(() => {
                hideModalForm();
                this.showCategoryManager();
            });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    categoryService = new CategoryService();
});
