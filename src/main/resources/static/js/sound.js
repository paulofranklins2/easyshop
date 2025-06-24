let bgmAudio;
let pipeAudio;
let coinAudio;
let checkoutAudio;
let logoutAudio;
let fireballAudio;
let jumpAudio;
let discountAudio;
const offcanvasTracked = new Set();
let navSoundEnabled = true;

function addOffcanvasListeners(root = document) {
    const elements = [];
    if(root.classList && root.classList.contains('offcanvas')) {
        elements.push(root);
    }
    elements.push(...root.querySelectorAll('.offcanvas'));
    elements.forEach(off => {
        if(!offcanvasTracked.has(off)) {
            off.addEventListener('show.bs.offcanvas', playNavSound);
            off.addEventListener('hide.bs.offcanvas', playNavSound);
            offcanvasTracked.add(off);
        }
    });
}

function playNavSound() {
    if (pipeAudio && navSoundEnabled) {
        pipeAudio.currentTime = 0;
        pipeAudio.play().catch(() => {});
    }
    navSoundEnabled = true;
}

function disableNextNavSound() {
    navSoundEnabled = false;
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

function playJumpSound() {
    if (jumpAudio) {
        jumpAudio.currentTime = 0;
        jumpAudio.play().catch(() => {});
    }
}

function playDiscountSound() {
    if (discountAudio) {
        discountAudio.currentTime = 0;
        discountAudio.play().catch(() => {});
    }
}



// initialize sounds on page load
document.addEventListener('DOMContentLoaded', () => {
    bgmAudio = new Audio('sounds/title-bgm.mp3');
    bgmAudio.loop = true;
    bgmAudio.volume = 0.15;
    bgmAudio.play().catch(() => {});

    pipeAudio = new Audio('sounds/super-mario-bros.mp3');
    coinAudio = new Audio('sounds/mario-coin-sound-effect.mp3');
    checkoutAudio = new Audio('sounds/1up.mp3');
    logoutAudio = new Audio('sounds/30_e6zdu9M.mp3');
    fireballAudio = new Audio('sounds/mario-fireball.mp3');
    jumpAudio = new Audio('sounds/super-mario-jump.mp3');
    discountAudio = new Audio('sounds/1-mushroom.mp3');

    // ensure non-bgm sounds play at full volume
    pipeAudio.volume = 1.0;
    coinAudio.volume = 1.0;
    checkoutAudio.volume = 1.0;
    logoutAudio.volume = 1.0;
    fireballAudio.volume = 1.0;
    jumpAudio.volume = 1.0;
    discountAudio.volume = 1.0;

    addOffcanvasListeners();

    // watch for dynamically added offcanvas elements
    const observer = new MutationObserver(() => addOffcanvasListeners());
    observer.observe(document.body, {childList: true, subtree: true});

    // resume background music on first user interaction if blocked
    const resumeBgm = () => {
        if (bgmAudio.paused) {
            bgmAudio.play().catch(() => {});
        }
    };
    document.addEventListener('click', resumeBgm, {once: true});
    document.addEventListener('keydown', resumeBgm, {once: true});
});
