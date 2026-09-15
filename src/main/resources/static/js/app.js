/* ==========================================================================
   Souq commerce console - shared client layer
   Modals, toasts, REST calls, and in-place grid refreshes.
   ========================================================================== */

const ICONS = {
    success: '<path d="M20 6 9 17l-5-5"/>',
    error: '<path d="M12 9v4"/><path d="M12 17h.01"/><path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0Z"/>',
    pencil: '<path d="M12 20h9"/><path d="M16.5 3.5a2.12 2.12 0 0 1 3 3L7 19l-4 1 1-4Z"/>',
    trash: '<path d="M3 6h18"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/><path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>'
};

function svg(paths, size = '3.5') {
    return `<svg class="h-${size} w-${size}" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="2" stroke-linecap="round" stroke-linejoin="round">${paths}</svg>`;
}

function escapeHtml(value) {
    if (value === null || value === undefined) return '';
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function formatMoney(value) {
    const amount = Number(value);
    if (!isFinite(amount)) return '0.00';
    return amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

/* --------------------------------------------------------------------------
   Toasts
   -------------------------------------------------------------------------- */

function showToast(message, type = 'success') {
    const host = document.getElementById('toastHost');
    if (!host) return;

    const accent = type === 'success'
        ? 'bg-emerald-500/15 text-emerald-300 ring-emerald-500/30'
        : 'bg-rose-500/15 text-rose-300 ring-rose-500/30';

    const toast = document.createElement('div');
    toast.className = 'toast-in surface pointer-events-auto flex w-80 items-start gap-3 rounded-xl border border-zinc-700 bg-zinc-900/95 p-4 shadow-2xl backdrop-blur-xl';
    toast.innerHTML = `
        <span class="mt-0.5 grid h-6 w-6 shrink-0 place-items-center rounded-lg ring-1 ring-inset ${accent}">
            ${svg(ICONS[type] || ICONS.success)}
        </span>
        <p class="flex-1 text-sm leading-snug text-zinc-100">${escapeHtml(message)}</p>
        <button type="button" class="shrink-0 rounded-md p-0.5 text-zinc-400 transition-colors hover:text-white" aria-label="Dismiss">
            ${svg('<path d="M18 6 6 18"/><path d="m6 6 12 12"/>', '4')}
        </button>`;

    const dismiss = () => {
        toast.classList.remove('toast-in');
        toast.classList.add('toast-out');
        setTimeout(() => toast.remove(), 220);
    };

    toast.querySelector('button').addEventListener('click', dismiss);
    host.appendChild(toast);
    setTimeout(dismiss, 4000);
}

/* --------------------------------------------------------------------------
   REST helpers
   -------------------------------------------------------------------------- */

async function apiCall(method, url, body) {
    const options = { method, headers: { 'Content-Type': 'application/json' } };
    if (body !== undefined) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    let payload = {};
    try {
        payload = await response.json();
    } catch (error) {
        payload = {};
    }

    if (!response.ok) {
        throw new Error(payload.message || `Request failed with status ${response.status}`);
    }
    return payload;
}

const loadProducts = () => apiCall('GET', '/api/v1/product/all-products').then(r => r.data || []);
const loadCategories = () => apiCall('GET', '/api/v1/category/all-categories').then(r => r.data || []);
const loadMerchants = () => apiCall('GET', '/api/v1/merchant/all-merchants').then(r => r.data || []);
const loadStocks = () => apiCall('GET', '/api/v1/merchantstock/all-merchantstock').then(r => r.data || []);
const loadUsers = () => apiCall('GET', '/api/v1/user/all-users').then(r => r.data || []);

/* --------------------------------------------------------------------------
   Modals
   -------------------------------------------------------------------------- */

function openModal(id) {
    const modal = document.getElementById(id);
    if (!modal) return;

    modal.classList.remove('hidden');
    document.body.classList.add('overflow-hidden');

    requestAnimationFrame(() => {
        modal.querySelector('[data-modal-backdrop]').classList.replace('opacity-0', 'opacity-100');
        const panel = modal.querySelector('[data-modal-panel]');
        panel.classList.remove('opacity-0', 'scale-95', 'translate-y-3');
        panel.classList.add('opacity-100', 'scale-100', 'translate-y-0');
    });

    const firstField = modal.querySelector('input:not([type="hidden"]):not([readonly]), select');
    if (firstField) setTimeout(() => firstField.focus(), 120);
}

function closeModal(id) {
    const modal = document.getElementById(id);
    if (!modal || modal.classList.contains('hidden')) return;

    modal.querySelector('[data-modal-backdrop]').classList.replace('opacity-100', 'opacity-0');
    const panel = modal.querySelector('[data-modal-panel]');
    panel.classList.remove('opacity-100', 'scale-100', 'translate-y-0');
    panel.classList.add('opacity-0', 'scale-95', 'translate-y-3');

    setTimeout(() => {
        modal.classList.add('hidden');
        if (!document.querySelector('[data-modal]:not(.hidden)')) {
            document.body.classList.remove('overflow-hidden');
        }
    }, 200);
}

function fillModal(id, values) {
    const modal = document.getElementById(id);
    if (!modal) return;
    Object.entries(values).forEach(([name, value]) => {
        const field = modal.querySelector(`[name="${name}"]`);
        if (field) field.value = value ?? '';
    });
    openModal(id);
}

document.addEventListener('click', (event) => {
    const opener = event.target.closest('[data-modal-open]');
    if (opener) {
        openModal(opener.dataset.modalOpen);
        return;
    }
    const closer = event.target.closest('[data-modal-close]');
    if (closer) {
        closeModal(closer.closest('[data-modal]').id);
        return;
    }
    if (event.target.matches('[data-modal-backdrop]')) {
        closeModal(event.target.closest('[data-modal]').id);
    }
});

document.addEventListener('keydown', (event) => {
    if (event.key !== 'Escape') return;
    const open = document.querySelector('[data-modal]:not(.hidden)');
    if (open) closeModal(open.id);
});

/* --------------------------------------------------------------------------
   Confirm dialog
   -------------------------------------------------------------------------- */

let pendingConfirm = null;

function confirmAction(title, message, onConfirm) {
    const modal = document.getElementById('confirmModal');
    if (!modal) {
        if (window.confirm(message)) onConfirm();
        return;
    }
    modal.querySelector('[data-confirm-title]').textContent = title;
    modal.querySelector('[data-confirm-message]').textContent = message;
    pendingConfirm = onConfirm;
    openModal('confirmModal');
}

document.addEventListener('DOMContentLoaded', () => {
    const button = document.querySelector('[data-confirm-accept]');
    if (!button) return;

    button.addEventListener('click', async () => {
        const action = pendingConfirm;
        pendingConfirm = null;
        closeModal('confirmModal');
        if (!action) return;
        try {
            await action();
        } catch (error) {
            showToast(error.message, 'error');
        }
    });
});

/* --------------------------------------------------------------------------
   Forms
   -------------------------------------------------------------------------- */

function bindForm(formId, handler) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const button = form.querySelector('[type="submit"]');
        const originalLabel = button.innerHTML;
        button.disabled = true;
        button.classList.add('opacity-60');
        button.innerHTML = 'Working...';

        try {
            await handler(new FormData(form), form);
        } catch (error) {
            showToast(error.message, 'error');
        } finally {
            button.disabled = false;
            button.classList.remove('opacity-60');
            button.innerHTML = originalLabel;
        }
    });
}

/* --------------------------------------------------------------------------
   Grid fragments shared with the server-rendered markup
   -------------------------------------------------------------------------- */

function emptyRow(columns, message) {
    return `
        <tr>
            <td colspan="${columns}" class="px-6 py-14 text-center text-sm text-zinc-400">${escapeHtml(message)}</td>
        </tr>`;
}

function tooltip(label, inner) {
    return `
        <span class="group/tip relative inline-flex">
            ${inner}
            <span class="pointer-events-none absolute -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap rounded-md border border-zinc-700 bg-zinc-900 px-2 py-1 text-xs text-zinc-100 opacity-0 shadow-xl transition-opacity duration-150 group-hover/tip:opacity-100">${label}</span>
        </span>`;
}

function actionButtons(noun) {
    return `
        <div class="flex items-center justify-end gap-1.5">
            ${tooltip('Edit', `<button type="button" aria-label="Edit ${noun}" class="js-edit icon-btn icon-btn-edit">${svg(ICONS.pencil)}</button>`)}
            ${tooltip('Delete', `<button type="button" aria-label="Delete ${noun}" class="js-delete icon-btn icon-btn-delete">${svg(ICONS.trash)}</button>`)}
        </div>`;
}

/* Initial letters used as a lightweight avatar in name columns. */
function initials(name) {
    return escapeHtml(String(name || '?').trim().charAt(0).toUpperCase());
}
