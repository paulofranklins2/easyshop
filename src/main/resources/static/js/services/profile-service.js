let profileService;

class ProfileService {
    loadProfile() {
        const url = `${config.baseUrl}/profile`;

        axios.get(url)
            .then(response => {
                const data = response.data;
                data.photoUrl = localStorage.getItem('profilePhotoUrl') || '';
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

                localStorage.setItem('profilePhotoUrl', document.getElementById('photoUrl').value);
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