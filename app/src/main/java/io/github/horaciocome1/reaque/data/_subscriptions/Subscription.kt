package io.github.horaciocome1.reaque.data._subscriptions

import io.github.horaciocome1.reaque.data._users.User


data class Subscription(var id: String) {

    var subscriber = User("")
    var subscribed = User("")

}