let ordersService;

class OrdersService {
    showOrders() {
        const url = `${config.baseUrl}/orders`;
        axios.get(url)
            .then(res => {
                const data = {orders: res.data, hasOrders: res.data.length > 0};
                templateBuilder.build('orders-offcanvas', data, 'ordersSidebarBody', () => {
                    const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('ordersSidebar'));
                    off.show();
                    document.querySelectorAll('.view-order').forEach(btn => {
                        btn.addEventListener('click', () => {
                            this.showOrderDetails(btn.dataset.id);
                        });
                    });
                });
            });
    }

    showOrderDetails(id) {
        axios.get(`${config.baseUrl}/orders/${id}`)
            .then(res => {
                const data = res.data;
                templateBuilder.build('order-detail-offcanvas', data, 'orderDetailSidebarBody', () => {
                    const listOff = bootstrap.Offcanvas.getInstance(document.getElementById('ordersSidebar'));
                    if (listOff) listOff.hide();
                    const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('orderDetailSidebar'));
                    off.show();
                });
            });
    }

    backToOrders() {
        const detailOff = bootstrap.Offcanvas.getInstance(document.getElementById('orderDetailSidebar'));
        if (detailOff) detailOff.hide();
        this.showOrders();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    ordersService = new OrdersService();
});