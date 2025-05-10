package com.plcoding.spring_boot_crash_course.controllers

import com.plcoding.spring_boot_crash_course.database.model.Note
import com.plcoding.spring_boot_crash_course.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

//Post: http://localhost:8085/notes
//http://localhost:8085
//Get : http://localhost:8085/notes?owerId=123

 @RestController()

class NoteController(
    private val repository: NoteRepository
) {

    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
    )

    data class NoteResponse(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant,
    )

    @PostMapping("notes/create")
    fun save(
      @RequestBody  body: NoteRequest
    ): NoteResponse {
        val note = repository.save(
            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerID = ObjectId(),
            ),
        )
        return note.toResponse();
    }

    @GetMapping("notes/get")
    fun findByOwnerID(
        @RequestParam(required = true) ownerId: String,
    ): List<NoteResponse> {
        return   repository.findByOwnerID(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }
}

private fun Note.toResponse(): NoteController.NoteResponse {
    return NoteController.NoteResponse(

        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt

    )
}