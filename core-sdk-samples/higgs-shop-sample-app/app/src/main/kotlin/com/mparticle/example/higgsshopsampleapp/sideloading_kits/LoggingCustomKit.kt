package com.mparticle.example.higgsshopsampleapp.sideloading_kits

import android.content.Context
import android.util.Log
import com.mparticle.MPEvent
import com.mparticle.internal.CoreCallbacks.KitListener
import com.mparticle.kits.KitIntegration.EventListener
import com.mparticle.kits.MPSideloadedKit
import com.mparticle.kits.ReportingMessage
import java.lang.Exception

class LoggingCustomKit(kitId: Int) : MPSideloadedKit(kitId), KitListener, EventListener {

    companion object {
        private const val CUSTOM_KIT = "LoggingCustomKit"
    }

    override fun getName(): String = CUSTOM_KIT

    override fun onKitCreate(
        settings: MutableMap<String, String>?,
        context: Context?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT onKitCreate")
        return mutableListOf()
    }

    override fun leaveBreadcrumb(breadcrumb: String?): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT leaveBreadcrumb with breadcrumb: ${breadcrumb.orEmpty()}")
        return mutableListOf()
    }

    override fun logError(
        message: String?,
        errorAttributes: MutableMap<String, String>?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT logError with message: ${message.orEmpty()}")
        return mutableListOf()
    }

    override fun logException(
        exception: Exception?,
        exceptionAttributes: MutableMap<String, String>?,
        message: String?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT logException with exception: ${exception?.message.orEmpty()} and message: ${message.orEmpty()}")
        return mutableListOf()
    }

    override fun logEvent(baseEvent: MPEvent): MutableList<ReportingMessage>? {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT logEvent with name: ${baseEvent.eventName}")
        return super.logEvent(baseEvent)
    }

    override fun logScreen(
        screenName: String?,
        screenAttributes: MutableMap<String, String>?
    ): MutableList<ReportingMessage> {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT logScreen with screen name: ${screenName.orEmpty()}")
        return mutableListOf()
    }

    override fun setOptOut(optedOut: Boolean): MutableList<ReportingMessage> = mutableListOf()

    override fun kitFound(kitId: Int) {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT kitFound for kit: $kitId")
    }

    override fun kitConfigReceived(kitId: Int, p1: String?) {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT kitConfigReceived for kit: $kitId")
    }

    override fun kitExcluded(kitId: Int, p1: String?) {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT kitExcluded for kit $kitId")
    }

    override fun kitStarted(kitId: Int) {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT kitStarted for kit: $kitId")
    }

    override fun onKitApiCalled(kitId: Int, p1: Boolean?, vararg p2: Any?) {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT onKitApiCalled for kit: $kitId")
    }

    override fun onKitApiCalled(methodName: String?, kitId: Int, p2: Boolean?, vararg p3: Any?) {
        Log.d(CUSTOM_KIT, "$CUSTOM_KIT onKitApiCalled for kit: $kitId with method name: ${methodName.orEmpty()}")
    }
}