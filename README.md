# APLIKASI DATA UPK — PPD Kanowit

Progressive Web App (PWA) shell for the Unit Pendidikan Khas, Pejabat Pendidikan Daerah Kanowit.

## Architecture

- **Frontend / hosting:** GitHub Pages
- **Existing application backend:** Google Apps Script
- **Database:** Existing Google Sheets
- **Original repository:** `MORRIS1971/DB_APLIKASI_UPK` (kept separate)

This repository contains only public static frontend assets. Do not copy Apps Script secrets, administrator PINs, access tokens, spreadsheet credentials, or private backend source here.

## Current status

The installable PWA shell, manifest, service worker, SVG icons, and GitHub Pages workflow are in place. The main-app launch URL is intentionally left blank in `config.js` until the currently deployed Apps Script web-app URL ending in `/exec` is confirmed.

The original Apps Script interface uses `google.script.run`. That bridge is available only inside Apps Script HTML Service pages and cannot be called directly from a GitHub Pages origin. The current safe first stage therefore prepares the PWA shell without pretending that Google Sheets read/write integration is already working.

## Data integrity baseline

The project owner has confirmed that the live Google Sheets data contains **15 portal records**. A previous issue caused by two columns being placed in the wrong order has already been corrected in the source spreadsheet. Treat the corrected live sheet as authoritative. Do not replace it with the 13-entry bootstrap/fallback array found in the legacy Apps Script source.

The PWA launcher must preserve the existing Apps Script application and its Google Sheets backend. The existing Apps Script HTML uses `google.script.run`, so its data operations remain inside the Apps Script-hosted application; this static GitHub Pages shell currently launches that application rather than attempting unsupported direct calls to `google.script.run`.

## Next integration gate

1. Confirm the production Apps Script deployment URL ending in `/exec`.
2. Verify the deployment and access model.
3. Select and test a secure integration design before enabling Admin operations.
4. Run real read/write/read-back tests against a controlled test record before any production handoff.

## GitHub Pages

The workflow in `.github/workflows/pages.yml` deploys the repository root to GitHub Pages when changes are pushed to `main`. In the repository settings, ensure Pages uses **GitHub Actions** as the build and deployment source if it is not already configured.

## Local checks

- Open `index.html` through a local static server (service workers do not run reliably from `file://`).
- Check that the manifest and both SVG icons load.
- Confirm the service worker installs and the shell opens offline.
- Do not treat the PWA as integrated until the Apps Script connection and Admin workflows pass real tests.
