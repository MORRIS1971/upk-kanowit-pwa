# Integrasi PWA dengan APLIKASI DATA UPK V2

Branch ini ialah staging sahaja. Branch `main` kekal menggunakan URL aplikasi semasa.

## Aliran sambungan yang dipilih
1. GitHub Pages memaparkan landing page PWA.
2. Butang Buka Aplikasi Utama membuka URL Apps Script V2 yang telah disahkan.
3. Semua operasi data dan Admin kekal dalam HTML Service Apps Script menggunakan `google.script.run`.
4. PWA tidak menyimpan PIN, token Admin, ID spreadsheet atau rahsia backend.

## Mengapa bukan panggilan terus dari GitHub Pages?
Frontend Apps Script V2 menggunakan `google.script.run`, iaitu jambatan HTML Service yang hanya wujud di halaman Apps Script. Memanggilnya terus daripada origin GitHub Pages bukan integrasi yang sah. Reka bentuk launcher mengelakkan dakwaan palsu bahawa PWA statik sudah mempunyai akses terus ke Google Sheets.

## Gerbang sebelum URL diaktifkan
- Deployment Apps Script V2 wujud dan menggunakan projek ujian.
- Identiti Google pengguna dan Admin diuji.
- 15 portal dibaca daripada salinan spreadsheet ujian.
- Ujian tulis-baca sebenar untuk CRUD, pengumuman dan perkongsian lulus.
- URL /exec disahkan.
- Barulah isi `appsScriptUrl` dalam `config.js` branch staging dan uji pemasangan PWA.

## Keselamatan penerbitan
Jangan merge branch ini ke main atau deploy staging ke GitHub Pages sebelum URL V2 disahkan. Jangan sentuh URL produksi atau spreadsheet produksi semasa ujian.
