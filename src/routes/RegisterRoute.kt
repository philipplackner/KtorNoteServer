package com.androiddevs.routes

import com.androiddevs.data.checkIfUserExists
import com.androiddevs.data.collections.User
import com.androiddevs.data.registerUser
import com.androiddevs.data.requests.AccountRequest
import com.androiddevs.data.responses.SimpleResponse
import com.androiddevs.security.getHashWithSalt
import io.ktor.application.call
import io.ktor.features.ContentTransformationException
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.cio.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Route.registerRoute() {
    route("/register") {
        post {
            withContext(Dispatchers.IO) {
                val request = try {
                    call.receive<AccountRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@withContext
                }
                val userExists = checkIfUserExists(request.email)

                if(!userExists) {
                    if(registerUser(User(request.email, getHashWithSalt(request.password)))) {
                        call.respond(OK, SimpleResponse(true, "Successfully created account!"))
                    } else {
                        call.respond(OK, SimpleResponse(false, "An unknown error occured"))
                    }
                } else {
                    call.respond(OK, SimpleResponse(false, "A user with that E-Mail already exists"))
                }
            }
        }
    }
}