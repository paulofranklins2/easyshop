let profileService;

class ProfileService {
    loadProfile() {
        const url = `${config.baseUrl}/profile`;

        axios.get(url)
            .then(response => {
                const data = response.data;
                templateBuilder.build("profile-offcanvas", data, "profileSidebarBody", () => {
                    const off = bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('profileSidebar'));
                    off.show();
                });
            })
            .catch(error => {
                const data = {
                    error: "Load profile failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    updateProfile(profile) {

        const url = `${config.baseUrl}/profile`;

        axios.put(url, profile)
            .then(() => {
                const data = {
                    message: "The profile has been updated."
                };

                templateBuilder.append("message", data, "errors");
            })
            .catch(error => {
                const data = {
                    error: "Save profile failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }
}

document.addEventListener("DOMContentLoaded", () => {
    profileService = new ProfileService();
});