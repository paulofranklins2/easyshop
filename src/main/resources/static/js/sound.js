let bgmAudio;
let navAudio;
let coinAudio;
let checkoutAudio;

function playNavSound() {
    if (navAudio) {
        navAudio.currentTime = 0;
        navAudio.play().catch(() => {});
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

// initialize sounds on page load
document.addEventListener('DOMContentLoaded', () => {
    bgmAudio = new Audio('sounds/title-bgm.mp3');
    bgmAudio.loop = true;
    bgmAudio.play().catch(() => {});

    navAudio = new Audio('sounds/super-mario-bros.mp3');
    coinAudio = new Audio('sounds/mario-coin-sound-effect.mp3');
    checkoutAudio = new Audio('sounds/30_e6zdu9M.mp3');

    // play sound when any navbar link or button is clicked
    document.body.addEventListener('click', (e) => {
        if (e.target.closest('.navbar a') || e.target.closest('.navbar button')) {
            playNavSound();
        }
    });

    // play sound when offcanvas is opened
    document.querySelectorAll('.offcanvas').forEach(off => {
        off.addEventListener('show.bs.offcanvas', () => {
            playNavSound();
        });
    });
});
