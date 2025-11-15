// Derive API base URL dynamically instead of hardcoding an IP.
// Priority: window.API_BASE_URL global -> <meta name="api-base-url" content="..."> -> window.location.origin + "/api" -> fallback "/api"
const config = (() => {
    try {
        const meta = (typeof document !== 'undefined') ? document.querySelector('meta[name="api-base-url"]') : null;
        const fromMeta = meta ? meta.getAttribute('content') : null;
        const fromGlobal = (typeof window !== 'undefined' && window.API_BASE_URL) ? String(window.API_BASE_URL) : null;
        const origin = (typeof window !== 'undefined' && window.location && window.location.origin) ? window.location.origin : '';

        const raw = fromGlobal || fromMeta || (origin ? `${origin}/api` : '/api');
        const normalized = String(raw).replace(/\/+$/, ''); // remove trailing slash(es)

        const cfg = { baseUrl: normalized };
        if (typeof window !== 'undefined') {
            window.config = cfg; // expose globally for scripts that rely on it
        }
        return cfg;
    } catch (e) {
        const cfg = { baseUrl: '/api' };
        if (typeof window !== 'undefined') {
            window.config = cfg;
        }
        return cfg;
    }
})();
