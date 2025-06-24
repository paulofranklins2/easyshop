let bgmAudio;
let pipeAudio;
let coinAudio;
let checkoutAudio;
let logoutAudio;
let fireballAudio;

function playNavSound() {
    if (pipeAudio) {
        pipeAudio.currentTime = 0;
        pipeAudio.play().catch(() => {});
    }
}

function playCoinSound() {
    if (coinAudio) {
        coinAudio.currentTime = 0;
        coinAudio.play().catch(() => {});
    }
}

function playCheckoutSound() {
    if (checkoutAudio) {
        checkoutAudio.currentTime = 0;
        checkoutAudio.play().catch(() => {});
    }
}

function playLogoutSound() {
    if (logoutAudio) {
        logoutAudio.currentTime = 0;
        logoutAudio.play().catch(() => {});
    }
}

function playFireballSound() {
    if (fireballAudio) {
        fireballAudio.currentTime = 0;
        fireballAudio.play().catch(() => {});
    }
}

// initialize sounds on page load
document.addEventListener('DOMContentLoaded', () => {
    bgmAudio = new Audio('sounds/title-bgm.mp3');
    bgmAudio.loop = true;
    bgmAudio.play().catch(() => {});

    pipeAudio = new Audio('sounds/super-mario-bros.mp3');
    coinAudio = new Audio('sounds/mario-coin-sound-effect.mp3');
    checkoutAudio = new Audio('sounds/1up.mp3');
    logoutAudio = new Audio('sounds/30_e6zdu9M.mp3');
    fireballAudio = new Audio('sounds/mario-fireball.mp3');

    // play sound when offcanvas is opened or closed
    document.querySelectorAll('.offcanvas').forEach(off => {
        off.addEventListener('show.bs.offcanvas', () => {
            playNavSound();
        });
        off.addEventListener('hide.bs.offcanvas', () => {
            playNavSound();
        });
    });
});
