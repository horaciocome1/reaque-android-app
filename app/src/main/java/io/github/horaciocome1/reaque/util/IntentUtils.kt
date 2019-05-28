package io.github.horaciocome1.reaque.util

import android.content.Intent
import android.net.Uri

fun getEmailIntent(address: String): Intent {
    val mailto = "mailto:$address"
    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse(mailto)
    return emailIntent
}