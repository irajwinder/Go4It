package com.example.go4it

import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class Payment : Application() {

    override fun onCreate() {
        super.onCreate()
        PayPalCheckout.setConfig(
            checkoutConfig = CheckoutConfig(
                application = this,
                clientId = "ARbm9T1qqxUk3GN-uLtbS9KRbVjhel_XdMGGUWGnzH23ybu5UWie3g4GdShaD_WtwI3yGwcJjaTXAgtj",
                environment = Environment.SANDBOX,
                currencyCode = CurrencyCode.USD,
                userAction = UserAction.PAY_NOW,
                settingsConfig = SettingsConfig(
                    loggingEnabled = true,
                    shouldFailEligibility = false
                )
            )
        )
    }
}
