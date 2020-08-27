package com.androiddevs.routes

import com.androiddevs.respondCss
import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import kotlinx.css.Color
import kotlinx.css.h3
import kotlinx.css.p
import kotlinx.css.px

fun Route.styleRoute() {
    route("/static/css/styles.css") {
        get {
            call.respondCss {
                p {
                    color = Color.green
                }
                h3 {
                    color = Color.red
                    backgroundColor = Color.blue
                    fontSize = 200.px
                }
                rule("div h2") {

                }
            }
        }
    }
}