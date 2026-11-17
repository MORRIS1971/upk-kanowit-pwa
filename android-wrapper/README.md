# Android wrapper — APLIKASI DATA UPK V2

This is a native Android WebView shell that opens the owner-supplied Apps Script V2 deployment. It is not a Trusted Web Activity and does not embed credentials. Google sign-in and Apps Script authorization depend on the deployment's Workspace policy and Android WebView compatibility.

## Build
The GitHub Actions workflow `Build UPK Android APK` builds a debug APK and uploads it as an artifact. Debug APK is for controlled testing only; a production release requires a managed signing key and signing configuration. Do not commit keystores or passwords.

Application ID: `my.gov.moe.upkkanowit`
