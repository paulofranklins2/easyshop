let userService;

class UserService {
    currentUser = {};

    constructor() {
        this.loadUser();
    }

    getHeader() {
        if (this.currentUser.token) {
            return {
                'Authorization': `Bearer ${this.currentUser.token}`
            };
        }

        return {};
    }

    saveUser(user) {
        this.currentUser = {
            token: user.token,
            userId: user.user.id,
            username: user.user.username,
            role: user.user.authorities[0].name
        }
        localStorage.setItem('user', JSON.stringify(this.currentUser));
    }

    loadUser() {
        const user = localStorage.getItem('user');
        if (user) {
            this.currentUser = JSON.parse(user);
            axios.defaults.headers.common = {'Authorization': `Bearer ${this.currentUser.token}`}
        }
    }

    getHeaders() {
        const headers = {
            'Content-Type': 'application/json'
        }

        if (this.currentUser.token) {
            headers.Authorization = `Bearer ${this.currentUser.token}`;
        }

        return headers;
    }

    getUserName() {
        return this.isLoggedIn() ? this.currentUser.username : '';
    }

    isLoggedIn() {
        return this.currentUser.token !== undefined;
    }

    getCurrentUser() {
        return this.currentUser;
    }

    setHeaderLogin() {
        const user = {
            username: this.getUserName(),
            loggedin: this.isLoggedIn(),
            loggedout: !this.isLoggedIn(),
            isAdmin: this.getCurrentUser().role === 'ROLE_ADMIN'
        };

        templateBuilder.build('header', user, 'header-user');
    }

    register(username, password, confirm, role, callback) {
        if (typeof role === 'function') {
            callback = role;
            role = 'USER';
        }
        if (!role) role = 'USER';

        const url = `${config.baseUrl}/register`;
        const register = {
            username: username,
            password: password,
            confirmPassword: confirm,
            role: role
        };

        axios.post(url, register)
            .then(response => {
                console.log(response.data);
                playCheckoutSound();
                if (typeof disableNextNavSound === 'function') {
                    disableNextNavSound();
                }
                if (typeof hideRegisterCanvas === 'function') {
                    hideRegisterCanvas();
                }
                this.login(username, password, false);
                if (callback) callback();
            })
            .catch(error => {

                const data = {
                    error: "User registration failed."
                };

                templateBuilder.append("error", data, "errors")
                playFireballSound();
            });
    }

    login(username, password, playSound = true, callback) {
        const url = `${config.baseUrl}/login`;
        const login = {
            username: username,
            password: password
        };

        axios.post(url, login)
            .then(response => {
                this.saveUser(response.data)
                this.setHeaderLogin();
                if (playSound) {
                    playCheckoutSound();
                }
                axios.defaults.headers.common = {'Authorization': `Bearer ${this.currentUser.token}`}
                productService.enableButtons();
                productService.search();
                cartService.loadCart();
                if (callback) callback();
            })
            .catch(error => {
                const data = {
                    error: "Login failed."
                };

                templateBuilder.append("error", data, "errors")
                playFireballSound();
            })
    }

    logout() {
        localStorage.removeItem('user');
        axios.defaults.headers.common = {'Authorization': `bearer ${this.currentUser.token}`}
        this.currentUser = {};
        playLogoutSound();

        this.setHeaderLogin();

        productService.enableButtons();
        productService.search();
    }

}

document.addEventListener('DOMContentLoaded', () => {
    userService = new UserService();
    userService.setHeaderLogin();
});
