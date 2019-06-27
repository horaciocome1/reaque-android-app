package io.github.horaciocome1.reaque.data.subscriptions

import io.github.horaciocome1.reaque.data.users.User


data class Subscription(var id: String) {

    var subscriber = User("")
    var user = User("")

}