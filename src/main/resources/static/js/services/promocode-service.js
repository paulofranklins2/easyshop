let promoCodeService;

class PromoCodeService {
    loadCodes() {
        axios.get(`${config.baseUrl}/promocodes`)
            .then(res => {
                const data = {codes: res.data, hasCodes: res.data.length > 0};
                templateBuilder.build('promo-codes-offcanvas', data, 'promoCodesSidebarBody', () => {
                    const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('promoCodesSidebar'));
                    off.show();
                });
            });
    }

    createCode() {
        axios.post(`${config.baseUrl}/promocodes`, {})
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
