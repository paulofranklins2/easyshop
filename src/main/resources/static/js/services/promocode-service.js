let promoCodeService;

class PromoCodeService {
    loadCodes() {
        const admin = userService.getCurrentUser().role === 'ROLE_ADMIN';
        const url = admin ? `${config.baseUrl}/promocodes` : `${config.baseUrl}/promocodes/history`;
        axios.get(url)
            .then(res => {
                const codes = res.data.map(c => ({...c, discountPercent: Math.round(c.discountPercent * 100)}));
                const data = {codes: codes, hasCodes: codes.length > 0, isAdmin: admin};
                templateBuilder.build('promo-codes-offcanvas', data, 'promoCodesSidebarBody', () => {
                    const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('promoCodesSidebar'));
                    off.show();
                    if (admin) {
                        document.querySelectorAll('.edit-code').forEach(btn => {
                            btn.addEventListener('click', () => this.enableEdit(btn.dataset.id));
                        });
                        document.querySelectorAll('.save-code').forEach(btn => {
                            btn.addEventListener('click', () => this.saveEdit(btn.dataset.id));
                        });
                        document.querySelectorAll('.cancel-edit').forEach(btn => {
                            btn.addEventListener('click', () => this.cancelEdit(btn.dataset.id));
                        });
                        document.querySelectorAll('.delete-code').forEach(btn => {
                            btn.addEventListener('click', () => this.deleteCode(btn.dataset.id));
                        });
                    }
                });
            });
    }

    createCode() {
        axios.post(`${config.baseUrl}/promocodes`, {})
            .then(() => this.loadCodes());
    }

    updateCode(id, percent) {
        axios.put(`${config.baseUrl}/promocodes/${id}?percent=${percent}`)
            .then(() => this.loadCodes());
    }

    deleteCode(id) {
        axios.delete(`${config.baseUrl}/promocodes/${id}`)
            .then(() => this.loadCodes());
    }

    applyCode() {
        const code = document.getElementById('promo-code-input').value;
        axios.post(`${config.baseUrl}/cart/promo/${code}`, {})
            .then(res => {
                cartService.setCart(res.data);
                cartService.renderSidebar();
                if (res.data.promoCode) {
                    playDiscountSound();
                }
            });
    }

    enableEdit(id) {
        const row = document.querySelector(`tr[data-id='${id}']`);
        if (!row) return;
        const input = row.querySelector('.code-discount');
        input.dataset.original = input.value;
        input.disabled = false;
        row.querySelector('.edit-code').classList.add('d-none');
        row.querySelector('.delete-code').classList.add('d-none');
        row.querySelector('.save-code').classList.remove('d-none');
        row.querySelector('.cancel-edit').classList.remove('d-none');
    }

    cancelEdit(id) {
        const row = document.querySelector(`tr[data-id='${id}']`);
        if (!row) return;
        const input = row.querySelector('.code-discount');
        input.value = input.dataset.original;
        input.disabled = true;
        row.querySelector('.edit-code').classList.remove('d-none');
        row.querySelector('.delete-code').classList.remove('d-none');
        row.querySelector('.save-code').classList.add('d-none');
        row.querySelector('.cancel-edit').classList.add('d-none');
    }

    saveEdit(id) {
        const row = document.querySelector(`tr[data-id='${id}']`);
        if (!row) return;
        const input = row.querySelector('.code-discount');
        const percent = parseFloat(input.value) / 100;
        this.updateCode(id, percent);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    promoCodeService = new PromoCodeService();
});
