package com.androiddevs.routes

import com.androiddevs.data.*
import com.androiddevs.data.collections.Note
import com.androiddevs.data.requests.AddOwnerRequest
import com.androiddevs.data.requests.DeleteNoteRequest
import com.androiddevs.data.responses.SimpleResponse
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.features.ContentTransformationException
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.noteRoutes() {
    route("/getNotes") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name

                val notes = getNotesForUser(email)
                call.respond(OK, notes)
            }
        }
    }
    route("/addOwnerToNote") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddOwnerRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(!checkIfUserExists(request.owner)) {
                    call.respond(
                        OK,
                        SimpleResponse(false, "No user with this E-Mail exists")
                    )
                    return@post
                }
                if(isOwnerOfNote(request.noteID, request.owner)) {
                    call.respond(
                        OK,
                        SimpleResponse(false, "This user is already an owner of this note")
                    )
                    return@post
                }
                if(addOwnerToNote(request.noteID, request.owner)) {
                    call.respond(
                        OK,
                        SimpleResponse(true, "${request.owner} can now see this note")
                    )
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
    route("/deleteNote") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteNoteRequest>()
                } catch(e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(deleteNoteForUser(email, request.id)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
    route("/addNote") {
        authenticate {
            post {
                val note = try {
                    call.receive<Note>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if(saveNote(note)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
}














