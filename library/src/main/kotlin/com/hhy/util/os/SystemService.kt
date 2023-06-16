package com.hhy.util.os

import android.accessibilityservice.AccessibilityService
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.DownloadManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.SearchManager
import android.app.UiModeManager
import android.app.WallpaperManager
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.role.RoleManager
import android.app.slice.SliceManager
import android.app.usage.NetworkStatsManager
import android.app.usage.StorageStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.companion.CompanionDeviceManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.ACCESSIBILITY_SERVICE
import android.content.Context.ACCOUNT_SERVICE
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.ALARM_SERVICE
import android.content.Context.APPWIDGET_SERVICE
import android.content.Context.APP_OPS_SERVICE
import android.content.Context.AUDIO_SERVICE
import android.content.Context.BATTERY_SERVICE
import android.content.Context.BIOMETRIC_SERVICE
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Context.CAMERA_SERVICE
import android.content.Context.CAPTIONING_SERVICE
import android.content.Context.CARRIER_CONFIG_SERVICE
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.COMPANION_DEVICE_SERVICE
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.CONSUMER_IR_SERVICE
import android.content.Context.CROSS_PROFILE_APPS_SERVICE
import android.content.Context.DEVICE_POLICY_SERVICE
import android.content.Context.DISPLAY_SERVICE
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Context.DROPBOX_SERVICE
import android.content.Context.EUICC_SERVICE
import android.content.Context.HARDWARE_PROPERTIES_SERVICE
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.INPUT_SERVICE
import android.content.Context.IPSEC_SERVICE
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Context.KEYGUARD_SERVICE
import android.content.Context.LAUNCHER_APPS_SERVICE
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.Context.MEDIA_PROJECTION_SERVICE
import android.content.Context.MEDIA_ROUTER_SERVICE
import android.content.Context.MEDIA_SESSION_SERVICE
import android.content.Context.MIDI_SERVICE
import android.content.Context.NETWORK_STATS_SERVICE
import android.content.Context.NFC_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context.NSD_SERVICE
import android.content.Context.POWER_SERVICE
import android.content.Context.PRINT_SERVICE
import android.content.Context.RESTRICTIONS_SERVICE
import android.content.Context.ROLE_SERVICE
import android.content.Context.SEARCH_SERVICE
import android.content.Context.SENSOR_SERVICE
import android.content.Context.SHORTCUT_SERVICE
import android.content.Context.STORAGE_SERVICE
import android.content.Context.STORAGE_STATS_SERVICE
import android.content.Context.SYSTEM_HEALTH_SERVICE
import android.content.Context.TELECOM_SERVICE
import android.content.Context.TELEPHONY_SERVICE
import android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE
import android.content.Context.TEXT_CLASSIFICATION_SERVICE
import android.content.Context.TEXT_SERVICES_MANAGER_SERVICE
import android.content.Context.TV_INPUT_SERVICE
import android.content.Context.UI_MODE_SERVICE
import android.content.Context.USAGE_STATS_SERVICE
import android.content.Context.USB_SERVICE
import android.content.Context.USER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Context.WALLPAPER_SERVICE
import android.content.Context.WIFI_AWARE_SERVICE
import android.content.Context.WIFI_P2P_SERVICE
import android.content.Context.WIFI_RTT_RANGING_SERVICE
import android.content.Context.WIFI_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.content.RestrictionsManager
import android.content.pm.CrossProfileApps
import android.content.pm.LauncherApps
import android.content.pm.ShortcutManager
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.biometrics.BiometricManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.IpSecManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.rtt.WifiRttManager
import android.nfc.NfcManager
import android.os.BatteryManager
import android.os.DropBoxManager
import android.os.HardwarePropertiesManager
import android.os.PowerManager
import android.os.UserManager
import android.os.Vibrator
import android.os.health.SystemHealthManager
import android.os.storage.StorageManager
import android.print.PrintManager
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.autofill.AutofillManager
import android.view.contentcapture.ContentCaptureManager
import android.view.inputmethod.InputMethodManager
import android.view.textclassifier.TextClassificationManager
import android.view.textservice.TextServicesManager
import androidx.annotation.RequiresApi
import com.hhy.util.appctx.appCtx

val AccessibilityService.windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager
val Context.windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager
val View.windowManager get() = context.windowManager
val windowManager: WindowManager get() = getSystemServiceAuto(WINDOW_SERVICE)
val Context.layoutInflater: LayoutInflater
    get() = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
val View.layoutInflater: LayoutInflater get() = context.layoutInflater
val activityManager: ActivityManager get() = getSystemServiceAuto(ACTIVITY_SERVICE)
val powerManager: PowerManager get() = getSystemServiceAuto(POWER_SERVICE)
val alarmManager: AlarmManager get() = getSystemServiceAuto(ALARM_SERVICE)
val notificationManager: NotificationManager get() = getSystemServiceAuto(NOTIFICATION_SERVICE)
val keyguardManager: KeyguardManager get() = getSystemServiceAuto(KEYGUARD_SERVICE)
val locationManager: LocationManager get() = getSystemServiceAuto(LOCATION_SERVICE)
val searchManager: SearchManager get() = getSystemServiceAuto(SEARCH_SERVICE)
val sensorManager: SensorManager get() = getSystemServiceAuto(SENSOR_SERVICE)
val storageManager: StorageManager get() = getSystemServiceAuto(STORAGE_SERVICE)

/** Null if invoked in an instant app. */
val wallpaperManager: WallpaperManager? get() = getSystemServiceAuto(WALLPAPER_SERVICE)
val vibrator: Vibrator get() = getSystemServiceAuto(VIBRATOR_SERVICE)
val connectivityManager: ConnectivityManager get() = getSystemServiceAuto(CONNECTIVITY_SERVICE)

/** Null if invoked in an instant app. */
val wifiManager: WifiManager?
    @SuppressLint("WifiManagerLeak") get() = getSystemServiceAuto(WIFI_SERVICE)

/** Null if invoked in an instant app. */
val wifiP2pManager: WifiP2pManager? get() = getSystemServiceAuto(WIFI_P2P_SERVICE)
val audioManager: AudioManager get() = getSystemServiceAuto(AUDIO_SERVICE)
val telephonyManager: TelephonyManager get() = getSystemServiceAuto(TELEPHONY_SERVICE)
val inputMethodManager: InputMethodManager get() = getSystemServiceAuto(INPUT_METHOD_SERVICE)
val downloadManager: DownloadManager get() = getSystemServiceAuto(DOWNLOAD_SERVICE)
val uiModeManager: UiModeManager get() = getSystemServiceAuto(UI_MODE_SERVICE)

/** Null if invoked in an instant app. */
val usbManager: UsbManager? get() = getSystemServiceAuto(USB_SERVICE)
val nfcManager: NfcManager get() = getSystemServiceAuto(NFC_SERVICE)

/** Null if invoked in an instant app. */
val devicePolicyManager: DevicePolicyManager? get() = getSystemServiceAuto(DEVICE_POLICY_SERVICE)
val textServicesManager: TextServicesManager
    get() = getSystemServiceAuto(TEXT_SERVICES_MANAGER_SERVICE)
val clipboardManager: ClipboardManager get() = getSystemServiceAuto(CLIPBOARD_SERVICE)
val accessibilityManager: AccessibilityManager get() = getSystemServiceAuto(ACCESSIBILITY_SERVICE)
val accountManager: AccountManager get() = getSystemServiceAuto(ACCOUNT_SERVICE)
val dropBoxManager: DropBoxManager get() = getSystemServiceAuto(DROPBOX_SERVICE)

val nsdManager: NsdManager @RequiresApi(16) get() = getSystemServiceAuto(NSD_SERVICE)
val mediaRouter: MediaRouter @RequiresApi(16) get() = getSystemServiceAuto(MEDIA_ROUTER_SERVICE)
val inputManager: InputManager @RequiresApi(16) get() = getSystemServiceAuto(INPUT_SERVICE)

val displayManager: DisplayManager @RequiresApi(17) get() = getSystemServiceAuto(DISPLAY_SERVICE)
val userManager: UserManager @RequiresApi(17) get() = getSystemServiceAuto(USER_SERVICE)

val bluetoothManager: BluetoothManager
    @RequiresApi(18) get() = getSystemServiceAuto(BLUETOOTH_SERVICE)

val appOpsManager: AppOpsManager @RequiresApi(19) get() = getSystemServiceAuto(APP_OPS_SERVICE)
val printManager: PrintManager @RequiresApi(19) get() = getSystemServiceAuto(PRINT_SERVICE)
val consumerIrManager: ConsumerIrManager
    @RequiresApi(19) get() = getSystemServiceAuto(CONSUMER_IR_SERVICE)
val captioningManager: CaptioningManager
    @RequiresApi(19) get() = getSystemServiceAuto(CAPTIONING_SERVICE)

val appWidgetManager: AppWidgetManager
    @RequiresApi(21) get() = getSystemServiceAuto(APPWIDGET_SERVICE)
val mediaSessionManager: MediaSessionManager
    @RequiresApi(21) get() = getSystemServiceAuto(MEDIA_SESSION_SERVICE)
val telecomManager: TelecomManager @RequiresApi(21) get() = getSystemServiceAuto(TELECOM_SERVICE)
val launcherApps: LauncherApps
    @RequiresApi(21) get() = getSystemServiceAuto(LAUNCHER_APPS_SERVICE)
val restrictionsManager: RestrictionsManager
    @RequiresApi(21) get() = getSystemServiceAuto(RESTRICTIONS_SERVICE)
val cameraManager: CameraManager @RequiresApi(21) get() = getSystemServiceAuto(CAMERA_SERVICE)
val tvInputManager: TvInputManager @RequiresApi(21) get() = getSystemServiceAuto(TV_INPUT_SERVICE)
val batteryManager: BatteryManager @RequiresApi(21) get() = getSystemServiceAuto(BATTERY_SERVICE)
val jobScheduler: JobScheduler
    @RequiresApi(21) get() = getSystemServiceAuto(JOB_SCHEDULER_SERVICE)
val mediaProjectionManager: MediaProjectionManager
    @RequiresApi(21) get() = getSystemServiceAuto(MEDIA_PROJECTION_SERVICE)

val usageStatsManager: UsageStatsManager
    @RequiresApi(22) get() = getSystemServiceAuto(USAGE_STATS_SERVICE)
val subscriptionManager: SubscriptionManager
    @RequiresApi(22) get() = getSystemServiceAuto(TELEPHONY_SUBSCRIPTION_SERVICE)

/** Null if invoked in an instant app. */
@Suppress("DeprecatedCallableAddReplaceWith", "DEPRECATION")
val networkStatsManager: NetworkStatsManager
    @RequiresApi(23) get() = getSystemServiceAuto(NETWORK_STATS_SERVICE)
val carrierConfigManager: CarrierConfigManager
    @RequiresApi(23) get() = getSystemServiceAuto(CARRIER_CONFIG_SERVICE)
val midiManager: MidiManager @RequiresApi(23) get() = getSystemServiceAuto(MIDI_SERVICE)

val hardwarePropertiesManager: HardwarePropertiesManager
    @RequiresApi(24) get() = getSystemServiceAuto(HARDWARE_PROPERTIES_SERVICE)
val systemHealthManager: SystemHealthManager
    @RequiresApi(24) get() = getSystemServiceAuto(SYSTEM_HEALTH_SERVICE)

/** Null if invoked in an instant app. */
val shortcutManager: ShortcutManager?
    @RequiresApi(25) get() = getSystemServiceAuto(SHORTCUT_SERVICE)

val companionDeviceManager: CompanionDeviceManager
    @RequiresApi(26) get() = getSystemServiceAuto(COMPANION_DEVICE_SERVICE)
val storageStatsManager: StorageStatsManager
    @RequiresApi(26) get() = getSystemServiceAuto(STORAGE_STATS_SERVICE)
val textClassificationManager: TextClassificationManager
    @RequiresApi(26) get() = getSystemServiceAuto(TEXT_CLASSIFICATION_SERVICE)

/** Null if invoked in an instant app. */
val wifiAwareManager: WifiAwareManager?
    @RequiresApi(26) get() = getSystemServiceAuto(WIFI_AWARE_SERVICE)
val autofillManager: AutofillManager
    @RequiresApi(26) get() = appCtx.getSystemService(AutofillManager::class.java)

val crossProfileApps: CrossProfileApps
    @RequiresApi(28) get() = getSystemServiceAuto(CROSS_PROFILE_APPS_SERVICE)
val euiccManager: EuiccManager @RequiresApi(28) get() = getSystemServiceAuto(EUICC_SERVICE)
val ipSecManager: IpSecManager @RequiresApi(28) get() = getSystemServiceAuto(IPSEC_SERVICE)
val wifiRttManager: WifiRttManager
    @RequiresApi(28) get() = getSystemServiceAuto(WIFI_RTT_RANGING_SERVICE)
val sliceManager: SliceManager
    @RequiresApi(28) get() = appCtx.getSystemService(SliceManager::class.java)

val biometricManager: BiometricManager
    @RequiresApi(29) get() = getSystemServiceAuto(BIOMETRIC_SERVICE)

val roleManager: RoleManager @RequiresApi(29) get() = getSystemServiceAuto(ROLE_SERVICE)
val contentCaptureManager: ContentCaptureManager
    @RequiresApi(29) get() = appCtx.getSystemService(ContentCaptureManager::class.java)

@Suppress("UNCHECKED_CAST")
internal fun <T> getSystemServiceAuto(name: String) = appCtx.getSystemService(name) as T
