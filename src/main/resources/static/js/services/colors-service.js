let colorService;

class ColorService {
    getAllColors(callback) {
        const url = `${config.baseUrl}/products/colors`;
        return axios.get(url)
            .then(response => {
                callback(response.data);
            })
            .catch(() => {
                const data = {
                    error: "Loading colors failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    colorService = new ColorService();
});
