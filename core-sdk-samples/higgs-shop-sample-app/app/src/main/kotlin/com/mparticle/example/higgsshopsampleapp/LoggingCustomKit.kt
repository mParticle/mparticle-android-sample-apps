package com.mparticle.example.higgsshopsampleapp

import android.content.Context
import android.util.Log
import com.mparticle.MPEvent
import com.mparticle.internal.CoreCallbacks.KitListener
import com.mparticle.kits.KitIntegration
import com.mparticle.kits.KitIntegration.EventListener
import com.mparticle.kits.MPSideloadedKit
import com.mparticle.kits.ReportingMessage
import java.lang.Exception

class LoggingCustomKit : MPSideloadedKit(), KitListener, EventListener {

    companion object {
        private const val CUSTOM_KIT = "Custom Kit"
    }

    override fun getName(): String = CUSTOM_KIT

    override fun onKitCreate(
        settings: MutableMap<String, String>?,
        context: Context?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT onKitCreate")
        return mutableListOf()
    }

    override fun leaveBreadcrumb(breadcrumb: String?): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
        return mutableListOf()
    }

    override fun logError(
        message: String?,
        errorAttributes: MutableMap<String, String>?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
        return mutableListOf()
    }

    override fun logException(
        exception: Exception?,
        exceptionAttributes: MutableMap<String, String>?,
        message: String?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
        return mutableListOf()
    }

    override fun logEvent(baseEvent: MPEvent): MutableList<ReportingMessage>? {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ${baseEvent.eventName}")
        return super.logEvent(baseEvent)
    }

    override fun logScreen(
        screenName: String?,
        screenAttributes: MutableMap<String, String>?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
        return mutableListOf()
    }

    override fun setOptOut(optedOut: Boolean): MutableList<ReportingMessage> = mutableListOf()
    override fun kitFound(p0: Int) {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
    }

    override fun kitConfigReceived(p0: Int, p1: String?) {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
    }

    override fun kitExcluded(p0: Int, p1: String?) {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
    }

    override fun kitStarted(p0: Int) {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
    }

    override fun onKitApiCalled(p0: Int, p1: Boolean?, vararg p2: Any?) {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
    }

    override fun onKitApiCalled(p0: String?, p1: Int, p2: Boolean?, vararg p3: Any?) {
        Log.d(CUSTOM_KIT, "CUSTOM_KIT event log with name ")
    }
}