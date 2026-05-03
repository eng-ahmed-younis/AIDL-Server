package com.printer.vendor

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.printer.vendor.model.ReceiptRequest

class PrinterRemoteService : Service() {

    private val binder = object : IPrinterService.Stub() {

        override fun isPrinterConnected(): Boolean {
            // Here vendor app checks Bluetooth/Wi-Fi/USB printer connection
            return true
        }


        override fun printReceipt(request: ReceiptRequest) {
            Log.d("PrinterRemoteService", "Receipt No: ${request.receiptNo}")
            Log.d("PrinterRemoteService", "Customer: ${request.customerName}")
            Log.d("PrinterRemoteService", "Total: ${request.totalAmount}")

            request.lines.forEach {
                Log.d("PrinterRemoteService", "Line: $it")
            }

            // Real printer logic here
            // printer.printText(...)
        }
    }

    // When another app binds to me, I will give it my Binder object.
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}

/**
 *
 * ❯❯❯ what is [stub]
 *
 * ⏺ What is Stub?
 *
 *   When Android compiles your .[aidl] file, it auto-generates a Java class — in your case [IPrinterService].java. Inside that
 *   generated class, there is a static abstract class called Stub.
 *
 *   [IPrinterService]          ← the AIDL interface
 *   └── [Stub]                 ← abstract class (extends Binder, implements IPrinterService)
 *       └── [Proxy]            ← used by the CLIENT side (auto-generated, you don't touch it)
 *
 *   What Stub does
 *
 *   Stub handles all the IPC (Inter-Process Communication) plumbing for you:
 *   - Receives calls from other processes over Binder
 *   - Unmarshals (deserializes) the arguments from a Parcel
 *   - Calls the right method on your implementation
 *   - Marshals (serializes) the return value back
 *
 *   Your job
 *
 *   You only need to extend Stub and implement the actual methods:
 *
 *   // SERVER side (your Service)
 *   private val binder = object : IPrinterService.Stub() {
 *       override fun isPrinterConnected(): Boolean = true
 *       override fun printReceipt(request: ReceiptRequest) { println(request.receiptNo) }
 *   }
 *
 *   The () matters
 *
 *   Stub is an abstract class, not an interface. In Kotlin:
 *   - object : SomeInterface { } — no () needed
 *   - object : AbstractClass() { } — () required to call the constructor
 *
 *   That's why IPrinterService.Stub (without ()) causes an error — Kotlin expects a constructor call.
 *
 *
 **/