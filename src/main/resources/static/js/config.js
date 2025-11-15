// Derive API base URL dynamically instead of hardcoding an IP.
// Priority: window.API_BASE_URL global -> <meta name="api-base-url" content="..."> -> window.location.origin + "/api" -> fallback "/api"
const config = (() => {
    try {
        const meta = (typeof document !== 'undefined') ? document.querySelector('meta[name="api-base-url"]') : null;
        const fromMeta = meta ? meta.getAttribute('content') : null;
        const fromGlobal = (typeof window !== 'undefined' && window.API_BASE_URL) ? String(window.API_BASE_URL) : null;
        const origin = (typeof window !== 'undefined' && window.location && window.location.origin) ? window.location.origin : '';
        // Try to respect a sub-path context (e.g., /easyshop) if app is hosted under one
        let contextPath = '';
        if (typeof window !== 'undefined' && window.location && typeof window.location.pathname === 'string') {
            const path = window.location.pathname;
            // If served under a base path like /easyshop/, include that in default API base
            const match = path.match(/^\/(?!js|css|images|fonts)([^\/]+)(?:\/|$)/);
            if (match && match[1] && match[1] !== 'index.html') {
                contextPath = `/${match[1]}`;
            }
        }

        const defaultBase = (origin ? `${origin}${contextPath}/api` : '/api');
        let raw = fromGlobal || fromMeta || defaultBase;
        // If an override points to localhost but the page isn't on localhost, ignore it
        try {
            if (typeof window !== 'undefined') {
                const pageHost = window.location && window.location.hostname;
                const isLocalHost = (h) => ['localhost', '127.0.0.1', '::1'].includes(String(h).toLowerCase());
                const parsed = new URL(raw, origin || (typeof window !== 'undefined' ? window.location.href : undefined));
                if (isLocalHost(parsed.hostname) && !isLocalHost(pageHost)) {
                    raw = defaultBase;
                    try { console.warn('Ignoring localhost API_BASE_URL on non-localhost page; using same-origin /api'); } catch (e) {}
                }
            }
        } catch (e) { /* non-absolute raw; fine */ }
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
