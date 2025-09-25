
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.content.negotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String, val name: String)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Required by Ktor configuration system
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message!!)
        }
    }

    routing {
        route("/users") {
            get("/{id}") {
                val userId = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing ID parameter."
                )

                if (userId.toIntOrNull()?.let { it > 0 } == true) {
                    call.respond(User(id = userId, name = "User $userId"))
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found.")
                }
            }

            post {
                try {
                    val newUser = call.receive<User>()
                    call.respond(HttpStatusCode.Created, "User created: ${newUser.name}")
                } catch (_) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request body format.")
                }
            }

            delete("/{id}") {
                val userId = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing ID parameter."
                )

                if (userId.toIntOrNull()?.let { it > 0 } == true) {
                    call.respond(HttpStatusCode.OK, "User deleted successfully.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found.")
                }
            }
        }

        get("/search") {
            val searchTerm = call.request.queryParameters["q"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Search term is required."
            )

            call.respond(mapOf("results" to listOf("Result 1", "Result 2")))
        }
    }
}
