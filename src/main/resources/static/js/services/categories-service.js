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
            const data = {
                categories: cats,
                hasCategories: cats.length > 0
            };
            templateBuilder.build('categories-offcanvas', data, 'categorySidebarBody', () => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('categorySidebar'));
                off.show();
            });
        });
    }

    showAddCategoryModal() {
        templateBuilder.build('add-category-offcanvas', {}, 'addCategorySidebarBody', () => {
            const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('addCategorySidebar'));
            off.show();
        });
    }

    createCategory() {
        const category = {
            name: document.getElementById('new-category-name').value,
            description: document.getElementById('new-category-description').value
        };
        axios.post(`${config.baseUrl}/categories`, category)
            .then(() => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('addCategorySidebar'));
                off.hide();
                this.showCategoryManager();
            });
    }

    showEditCategoryModal(id, name, description) {
        templateBuilder.build('edit-category-offcanvas', {categoryId: id, name, description}, 'editCategorySidebarBody', () => {
            const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('editCategorySidebar'));
            off.show();
        });
    }

    updateCategory(id) {
        const category = {
            name: document.getElementById('edit-category-name').value,
            description: document.getElementById('edit-category-description').value
        };
        axios.put(`${config.baseUrl}/categories/${id}`, category)
            .then(() => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('editCategorySidebar'));
                off.hide();
                this.showCategoryManager();
            });
    }

    showDeleteCategoryModal(id, name, description) {
        const data = {categoryId: id, name, description};
        templateBuilder.build('confirm-delete-category-offcanvas', data, 'deleteCategorySidebarBody', () => {
            const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('deleteCategorySidebar'));
            off.show();
        });
    }

    deleteCategory(id) {
        axios.delete(`${config.baseUrl}/categories/${id}`)
            .then(() => {
                const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('deleteCategorySidebar'));
                off.hide();
                this.showCategoryManager();
            });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    categoryService = new CategoryService();
});
