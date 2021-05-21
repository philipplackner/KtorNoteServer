package com.androiddevs.routes

import com.androiddevs.respondCss
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import kotlinx.css.*

fun Route.styleRoute() {
    route("/static/css/styles.css") {
        get {
            call.respondCss {
                p {
                    color = Color.green
                    fontSize = 200.px
                }
                rule("div h2") {
                    backgroundColor = Color("#303030")
                    borderWidth = 5.em
                }
            }
        }
    }
}