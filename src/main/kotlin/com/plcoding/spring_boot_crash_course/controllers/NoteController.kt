package com.plcoding.spring_boot_crash_course.controllers

import com.plcoding.spring_boot_crash_course.database.model.Note
import com.plcoding.spring_boot_crash_course.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.hibernate.validator.cfg.defs.NotEmptyDef
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.awt.Color
import java.time.Instant

// https://notes.com/notes

@RestController("/notes")
class NoteController(
    private val repository: NoteRepository
) {

    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
        val ownerId: String,
    )

    data class NoteResponse(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )
@PostMapping()
    fun save(body: NoteRequest):NoteResponse {
    val note =    repository.save(
            Note(
                id =body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerID = ObjectId(body.ownerId)


            )
        )
    return NoteResponse(
        id = note.id.toHexString(),
        title = note.title,
        content = note.content,
        color = note.color,
        createdAt = note.createdAt
    )
    }
    @GetMapping("/{id}")
    fun findByOwnerId(
         @RequestParam(required = true) ownerId: String,
    ):List<NoteResponse>{
        return  repository.findByOwnerId(ObjectId(ownerId)).map{
            NoteResponse(
                ownerId
            )
        }

    }
}