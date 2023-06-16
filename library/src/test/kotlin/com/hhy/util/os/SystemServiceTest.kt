package com.hhy.util.os

import android.accessibilityservice.AccessibilityService
import android.accounts.AccountManager
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
import androidx.test.filters.MediumTest
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertSame

@MediumTest
class SystemServiceTest {
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun windowManager_shouldWork_whenInAccessibility() {
        val windowManager = mockk<WindowManager>()
        val accessibilityService = mockk<AccessibilityService> {
            every { getSystemService(any()) } returns windowManager
        }
        assertSame(windowManager, accessibilityService.windowManager)
        verify { accessibilityService.getSystemService(any()) }
    }

    @Test
    fun windowManager_shouldWork_whenInContext() {
        val windowManager = mockk<WindowManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns windowManager
        }
        assertSame(windowManager, context.windowManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun windowManager_shouldWork_whenInView() {
        val windowManager = mockk<WindowManager>()
        val view = mockk<View> {
            every { context.getSystemService(any()) } returns windowManager
        }
        assertSame(windowManager, view.windowManager)
        verify { view.context.getSystemService(any()) }
    }

    @Test
    fun windowManager_shouldWork_whenInGlobal() {
        val wm = mockk<WindowManager>()
        val context = mockk<Context> {
            every { getSystemService(Context.WINDOW_SERVICE) } returns wm
        }
        context.injectAsAppCtx()

        assertSame(wm, windowManager)
        verify { context.getSystemService(Context.WINDOW_SERVICE) }
    }

    @Test
    fun layoutInflater_shouldWork_whenInContext() {
        val layoutInflater = mockk<LayoutInflater>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns layoutInflater
        }
        assertSame(layoutInflater, context.layoutInflater)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun layoutInflater_shouldWork_whenInView() {
        val layoutInflater = mockk<LayoutInflater>()
        val view = mockk<View> {
            every { context.getSystemService(any()) } returns layoutInflater
        }
        assertSame(layoutInflater, view.layoutInflater)
        verify { view.context.getSystemService(any()) }
    }

    @Test
    fun activityManager_shouldWork_whenInGlobal() {
        val am = mockk<ActivityManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns am
        }
        context.injectAsAppCtx()

        assertSame(am, activityManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun powerManager_shouldWork_whenInGlobal() {
        val pm = mockk<PowerManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns pm
        }
        context.injectAsAppCtx()

        assertSame(pm, powerManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun alarmManager_shouldWork_whenInGlobal() {
        val am = mockk<AlarmManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns am
        }
        context.injectAsAppCtx()

        assertSame(am, alarmManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun notificationManager_shouldWork_whenInGlobal() {
        val nm = mockk<NotificationManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns nm
        }
        context.injectAsAppCtx()

        assertSame(nm, notificationManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun keyguardManager_shouldWork_whenInGlobal() {
        val km = mockk<KeyguardManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns km
        }
        context.injectAsAppCtx()

        assertSame(km, keyguardManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun locationManager_shouldWork_whenInGlobal() {
        val lm = mockk<LocationManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns lm
        }
        context.injectAsAppCtx()

        assertSame(lm, locationManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun searchManager_shouldWork_whenInGlobal() {
        val sm = mockk<SearchManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns sm
        }
        context.injectAsAppCtx()

        assertSame(sm, searchManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun sensorManager_shouldWork_whenInGlobal() {
        val sm = mockk<SensorManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns sm
        }
        context.injectAsAppCtx()

        assertSame(sm, sensorManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun storageManager_shouldWork_whenInGlobal() {
        val sm = mockk<StorageManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns sm
        }
        context.injectAsAppCtx()

        assertSame(sm, storageManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun wallpaperManager_shouldWork_whenInGlobal() {
        val wm = mockk<WallpaperManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns wm
        }
        context.injectAsAppCtx()

        assertSame(wm, wallpaperManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun vibrator_shouldWork_whenInGlobal() {
        val vib = mockk<Vibrator>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns vib
        }
        context.injectAsAppCtx()

        assertSame(vib, vibrator)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun connectivityManager_shouldWork_whenInGlobal() {
        val cm = mockk<ConnectivityManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns cm
        }
        context.injectAsAppCtx()

        assertSame(cm, connectivityManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun wifiManager_shouldWork_whenInGlobal() {
        val wm = mockk<WifiManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns wm
        }
        context.injectAsAppCtx()

        assertSame(wm, wifiManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun wifiP2pManager_shouldWork_whenInGlobal() {
        val wpm = mockk<WifiP2pManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns wpm
        }
        context.injectAsAppCtx()

        assertSame(wpm, wifiP2pManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun audioManager_shouldWork_whenInGlobal() {
        val am = mockk<AudioManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns am
        }
        context.injectAsAppCtx()

        assertSame(am, audioManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun telephonyManager_shouldWork_whenInGlobal() {
        val tm = mockk<TelephonyManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns tm
        }
        context.injectAsAppCtx()

        assertSame(tm, telephonyManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun inputMethodManager_shouldWork_whenInGlobal() {
        val imm = mockk<InputMethodManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns imm
        }
        context.injectAsAppCtx()

        assertSame(imm, inputMethodManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun downloadManager_shouldWork_whenInGlobal() {
        val dm = mockk<DownloadManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns dm
        }
        context.injectAsAppCtx()

        assertSame(dm, downloadManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun uiModeManager_shouldWork_whenInGlobal() {
        val umm = mockk<UiModeManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns umm
        }
        context.injectAsAppCtx()

        assertSame(umm, uiModeManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun usbManager_shouldWork_whenInGlobal() {
        val um = mockk<UsbManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns um
        }
        context.injectAsAppCtx()

        assertSame(um, usbManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun nfcManager_shouldWork_whenInGlobal() {
        val nm = mockk<NfcManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns nm
        }
        context.injectAsAppCtx()

        assertSame(nm, nfcManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun devicePolicyManager_shouldWork_whenInGlobal() {
        val dpm = mockk<DevicePolicyManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns dpm
        }
        context.injectAsAppCtx()

        assertSame(dpm, devicePolicyManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun textServicesManager_shouldWork_whenInGlobal() {
        val tsm = mockk<TextServicesManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns tsm
        }
        context.injectAsAppCtx()

        assertSame(tsm, textServicesManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun clipboardManager_shouldWork_whenInGlobal() {
        val cm = mockk<ClipboardManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns cm
        }
        context.injectAsAppCtx()

        assertSame(cm, clipboardManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun accessibilityManager_shouldWork_whenInGlobal() {
        val am = mockk<AccessibilityManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns am
        }
        context.injectAsAppCtx()

        assertSame(am, accessibilityManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun accountManager_shouldWork_whenInGlobal() {
        val am = mockk<AccountManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns am
        }
        context.injectAsAppCtx()

        assertSame(am, accountManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun dropBoxManager_shouldWork_whenInGlobal() {
        val dbm = mockk<DropBoxManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns dbm
        }
        context.injectAsAppCtx()

        assertSame(dbm, dropBoxManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun nsdManager_shouldWork_whenInGlobal() {
        val nm = mockk<NsdManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns nm
        }
        context.injectAsAppCtx()

        assertSame(nm, nsdManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun mediaRouter_shouldWork_whenInGlobal() {
        val mr = mockk<MediaRouter>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns mr
        }
        context.injectAsAppCtx()

        assertSame(mr, mediaRouter)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun inputManager_shouldWork_whenInGlobal() {
        val im = mockk<InputManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns im
        }
        context.injectAsAppCtx()

        assertSame(im, inputManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun displayManager_shouldWork_whenInGlobal() {
        val dm = mockk<DisplayManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns dm
        }
        context.injectAsAppCtx()

        assertSame(dm, displayManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun userManager_shouldWork_whenInGlobal() {
        val um = mockk<UserManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns um
        }
        context.injectAsAppCtx()

        assertSame(um, userManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun bluetoothManager_shouldWork_whenInGlobal() {
        val bm = mockk<BluetoothManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns bm
        }
        context.injectAsAppCtx()

        assertSame(bm, bluetoothManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun appOpsManager_shouldWork_whenInGlobal() {
        val aom = mockk<AppOpsManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns aom
        }
        context.injectAsAppCtx()

        assertSame(aom, appOpsManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun printManager_shouldWork_whenInGlobal() {
        val pm = mockk<PrintManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns pm
        }
        context.injectAsAppCtx()

        assertSame(pm, printManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun consumerIrManager_shouldWork_whenInGlobal() {
        val cim = mockk<ConsumerIrManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns cim
        }
        context.injectAsAppCtx()

        assertSame(cim, consumerIrManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun captioningManager_shouldWork_whenInGlobal() {
        val cm = mockk<CaptioningManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns cm
        }
        context.injectAsAppCtx()

        assertSame(cm, captioningManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun appWidgetManager_shouldWork_whenInGlobal() {
        val awm = mockk<AppWidgetManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns awm
        }
        context.injectAsAppCtx()

        assertSame(awm, appWidgetManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun mediaSessionManager_shouldWork_whenInGlobal() {
        val msm = mockk<MediaSessionManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns msm
        }
        context.injectAsAppCtx()

        assertSame(msm, mediaSessionManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun telecomManager_shouldWork_whenInGlobal() {
        val tm = mockk<TelecomManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns tm
        }
        context.injectAsAppCtx()

        assertSame(tm, telecomManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun launcherApps_shouldWork_whenInGlobal() {
        val la = mockk<LauncherApps>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns la
        }
        context.injectAsAppCtx()

        assertSame(la, launcherApps)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun restrictionsManager_shouldWork_whenInGlobal() {
        val rm = mockk<RestrictionsManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns rm
        }
        context.injectAsAppCtx()

        assertSame(rm, restrictionsManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun cameraManager_shouldWork_whenInGlobal() {
        val cm = mockk<CameraManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns cm
        }
        context.injectAsAppCtx()

        assertSame(cm, cameraManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun tvInputManager_shouldWork_whenInGlobal() {
        val tim = mockk<TvInputManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns tim
        }
        context.injectAsAppCtx()

        assertSame(tim, tvInputManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun batteryManager_shouldWork_whenInGlobal() {
        val bm = mockk<BatteryManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns bm
        }
        context.injectAsAppCtx()

        assertSame(bm, batteryManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun jobScheduler_shouldWork_whenInGlobal() {
        val js = mockk<JobScheduler>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns js
        }
        context.injectAsAppCtx()

        assertSame(js, jobScheduler)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun mediaProjectionManager_shouldWork_whenInGlobal() {
        val mpm = mockk<MediaProjectionManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns mpm
        }
        context.injectAsAppCtx()

        assertSame(mpm, mediaProjectionManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun usageStatsManager_shouldWork_whenInGlobal() {
        val usm = mockk<UsageStatsManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns usm
        }
        context.injectAsAppCtx()

        assertSame(usm, usageStatsManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun subscriptionManager_shouldWork_whenInGlobal() {
        val sm = mockk<SubscriptionManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns sm
        }
        context.injectAsAppCtx()

        assertSame(sm, subscriptionManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun networkStatsManager_shouldWork_whenInGlobal() {
        val nsm = mockk<NetworkStatsManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns nsm
        }
        context.injectAsAppCtx()

        assertSame(nsm, networkStatsManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun carrierConfigManager_shouldWork_whenInGlobal() {
        val ccm = mockk<CarrierConfigManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns ccm
        }
        context.injectAsAppCtx()

        assertSame(ccm, carrierConfigManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun midiManager_shouldWork_whenInGlobal() {
        val mm = mockk<MidiManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns mm
        }
        context.injectAsAppCtx()

        assertSame(mm, midiManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun hardwarePropertiesManager_shouldWork_whenInGlobal() {
        val hpm = mockk<HardwarePropertiesManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns hpm
        }
        context.injectAsAppCtx()

        assertSame(hpm, hardwarePropertiesManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun systemHealthManager_shouldWork_whenInGlobal() {
        val shm = mockk<SystemHealthManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns shm
        }
        context.injectAsAppCtx()

        assertSame(shm, systemHealthManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun shortcutManager_shouldWork_whenInGlobal() {
        val sm = mockk<ShortcutManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns sm
        }
        context.injectAsAppCtx()

        assertSame(sm, shortcutManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun companionDeviceManager_shouldWork_whenInGlobal() {
        val sdm = mockk<CompanionDeviceManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns sdm
        }
        context.injectAsAppCtx()

        assertSame(sdm, companionDeviceManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun storageStatsManager_shouldWork_whenInGlobal() {
        val ssm = mockk<StorageStatsManager>()
        val context = mockk<Context> {
            every { getSystemService(Context.STORAGE_STATS_SERVICE) } returns ssm
        }
        context.injectAsAppCtx()

        assertSame(ssm, storageStatsManager)
        verify { context.getSystemService(Context.STORAGE_STATS_SERVICE) }
    }

    @Test
    fun textClassificationManager_shouldWork_whenInGlobal() {
        val tcm = mockk<TextClassificationManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns tcm
        }
        context.injectAsAppCtx()

        assertSame(tcm, textClassificationManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun wifiAwareManager_shouldWork_whenInGlobal() {
        val wam = mockk<WifiAwareManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns wam
        }
        context.injectAsAppCtx()

        assertSame(wam, wifiAwareManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun autofillManager_shouldWork_whenInGlobal() {
        val am = mockk<AutofillManager>()
        val context = mockk<Context> {
            every { getSystemService(AutofillManager::class.java) } returns am
        }
        context.injectAsAppCtx()
        assertSame(am, autofillManager)
        verify { context.getSystemService(AutofillManager::class.java) }
    }

    @Test
    fun crossProfileApps_shouldWork_whenInGlobal() {
        val cpa = mockk<CrossProfileApps>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns cpa
        }
        context.injectAsAppCtx()

        assertSame(cpa, crossProfileApps)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun euiccManager_shouldWork_whenInGlobal() {
        val em = mockk<EuiccManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns em
        }
        context.injectAsAppCtx()

        assertSame(em, euiccManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun ipSecManager_shouldWork_whenInGlobal() {
        mockkStatic("com.hhy.util.os.SystemServiceKt")
        val ism = mockk<IpSecManager>()
        every { getSystemServiceAuto<IpSecManager>(any()) } returns ism
        assertSame(ism, ipSecManager)
    }

    @Test
    fun wifiRttManager_shouldWork_whenInGlobal() {
        val wrm = mockk<WifiRttManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns wrm
        }
        context.injectAsAppCtx()

        assertSame(wrm, wifiRttManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun sliceManager_shouldWork_whenInGlobal() {
        val sm = mockk<SliceManager>()
        val context = mockk<Context> {
            every { getSystemService(SliceManager::class.java) } returns sm
        }
        context.injectAsAppCtx()

        assertSame(sm, sliceManager)
        verify { context.getSystemService(SliceManager::class.java) }
    }

    @Test
    fun biometricManager_shouldWork_whenInGlobal() {
        val bm = mockk<BiometricManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns bm
        }
        context.injectAsAppCtx()

        assertSame(bm, biometricManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun roleManager_shouldWork_whenInGlobal() {
        val rm = mockk<RoleManager>()
        val context = mockk<Context> {
            every { getSystemService(any()) } returns rm
        }
        context.injectAsAppCtx()

        assertSame(rm, roleManager)
        verify { context.getSystemService(any()) }
    }

    @Test
    fun contentCaptureManager_shouldWork_whenInGlobal() {
        val ccm = mockk<ContentCaptureManager>()
        val context = mockk<Context> {
            every { getSystemService(ContentCaptureManager::class.java) } returns ccm
        }
        context.injectAsAppCtx()

        assertSame(ccm, contentCaptureManager)
        verify { context.getSystemService(ContentCaptureManager::class.java) }
    }
}
