# FamilyNotes Android (kiosk shell)

Android app that runs the **FamilyNotes viewing experience** full screen on a tablet: a **WebView** pointed at the web app’s device/kiosk URL. Built for **in-home displays** where the recipient should not need to navigate the OS—typical setup is an **Android tablet**, including **commercial digital frames that are Android tablets under the hood** (repurposed or reflashed for FamilyNotes).

Sibling repos: **`FamilyNotes`** (web app + API), **`familynotes-promo`** (marketing site).

## What it does

- Loads **`https://app.familynotes.net/device`** (configurable in `WebViewScreen.kt`).
- **Wi‑Fi required:** If the device does not have Wi‑Fi with validated internet access, the app shows a prompt to open system connectivity settings instead of a broken WebView.
- **Kiosk-style presentation:** Keeps the screen on, immersive/system bars hidden (swipe to reveal), edge-to-edge layout.
- **WebView:** JavaScript and DOM storage enabled for the web app; zoom disabled; mixed content blocked.
- **Observability:** Sentry (errors, performance, session replay)—review sample rates for production.

`AndroidManifest.xml` uses `lockTaskMode="if_whitelisted"` so the activity can participate in **screen pinning / device-owner kiosk** deployments when you configure that on the device.

## Requirements

- **minSdk 29** (Android 10+)
- **JDK 17** for CI (see `.github/workflows/android.yml`)
- Android Studio / Gradle wrapper included (`./gradlew`)

## Build

```bash
chmod +x gradlew   # if needed
./gradlew build
```

Release builds use **`WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)`** (debugging off in release). **Sentry** sampling and privacy defaults are set in **`AndroidManifest.xml`** for kiosk/shared-device use (review DSN handling if the repo is public).

## Hardware note (operator-provisioned tablets)

FamilyNotes can ship or hand-tune **specific Android tablets**, including units originally sold as **Wi‑Fi digital frames**. Those devices are standard Android under vendor skins; this APK is the **controlled shell** that opens only the FamilyNotes kiosk URL. That keeps the recipient path “look at the screen,” while **you** still manage provisioning, Wi‑Fi, updates, and support on a **known device class**—not arbitrary BYOD tablets from customers.

## Project layout

| Path | Role |
|------|------|
| `app/src/main/java/.../MainActivity.kt` | Connectivity gate, kiosk window flags |
| `app/src/main/java/.../webview/WebViewScreen.kt` | WebView + loading UI |
| `app/src/main/java/.../wifi/WifiPromptScreen.kt` | Offline / no Wi‑Fi UX |

## CI

Pushes and pull requests to `main` run `./gradlew build` on Ubuntu.
