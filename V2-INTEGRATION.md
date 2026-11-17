# UPK Kanowit PWA — V2 staging

## Architecture
GitHub Pages PWA is the installable launcher and app shell. Google Apps Script V2 is the application runtime because the frontend uses Google Apps Script HTML Service and `google.script.run`. Google Sheets remains the data store.

## Staging safety
- This branch is isolated: `upk-v2-staging`.
- `config.js` intentionally keeps `appsScriptUrl` empty until a separate V2 Apps Script deployment has passed runtime tests.
- Never put PINs, tokens, spreadsheet IDs, OAuth secrets or other credentials in this public repository.
- Do not merge this branch into `main` before end-to-end validation.
- Production Apps Script URL and production spreadsheet are not modified.

## PWA validation
The GitHub Pages workflow validates JavaScript syntax, manifest JSON, required shell files and service-worker registration. The service worker uses a versioned staging cache, network-first navigation and cache-first static assets with network fallback.

Current icons are SVG. They render in supported browsers, but Android install surfaces and Trusted Web Activity packaging may require raster PNG icons. Add 192x192 and 512x512 PNG assets before final Android release and verify installability on target devices.

## V2 release gates
1. Deploy the isolated Apps Script V2 project as a Web App.
2. Confirm `pingPortal()`, signed-in identity, admin authentication, read/write behavior and Drive permission behavior against test data.
3. Set the verified V2 `/exec` URL in `config.js` on this staging branch only.
4. Run GitHub Pages workflow and test desktop and Android launch.
5. Confirm the app opens the V2 deployment and all required workflows work.
6. Build Android wrapper/TWA only after the HTTPS PWA origin and V2 URL are stable. Validate asset links, signing, install, back navigation and offline shell.
7. Only after acceptance, plan a separate production release. Never replace production as part of staging.

## Current state
- V2 URL: intentionally unset.
- Backend runtime tests: pending isolated Google Apps Script project and test spreadsheet.
- PWA static source checks: run in GitHub Actions.
- APK: not built or signed yet.
