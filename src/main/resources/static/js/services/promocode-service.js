let promoCodeService;

class PromoCodeService {
    loadCodes() {
        const admin = userService.getCurrentUser().role === 'ROLE_ADMIN';
        const url = admin ? `${config.baseUrl}/promocodes` : `${config.baseUrl}/promocodes/history`;
        axios.get(url)
            .then(res => {
                const data = {codes: res.data, hasCodes: res.data.length > 0, isAdmin: admin};
                templateBuilder.build('promo-codes-offcanvas', data, 'promoCodesSidebarBody', () => {
                    const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('promoCodesSidebar'));
                    off.show();
                    if (admin) {
                        document.querySelectorAll('.code-discount').forEach(inp => {
                            inp.addEventListener('change', () => this.updateCode(inp.dataset.id, inp.value));
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
            });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    promoCodeService = new PromoCodeService();
});
