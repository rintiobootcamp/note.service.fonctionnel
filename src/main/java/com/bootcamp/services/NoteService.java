package com.bootcamp.services;

import com.bootcamp.Helper.NoteHelper;
import com.bootcamp.commons.constants.DatabaseConstants;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.enums.NoteType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.usecases.pivotone.NoteWS;
import com.bootcamp.crud.NoteCRUD;
import com.bootcamp.entities.Note;
//import org.eclipse.persistence.internal.helper.Helper;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class NoteService implements DatabaseConstants {

    NoteHelper noteHelper = new NoteHelper();

    /**
     * Insert a note in the database
     *
     * @param note
     * @return the note id
     * @throws SQLException
     */
    public Note create(Note note) throws SQLException {
        note.setDateCreation(System.currentTimeMillis());
        NoteCRUD.create(note);
        return note;
    }

    /**
     * Update a note in the database
     *
     * @param note
     * @return id
     * @throws SQLException
     */
    public int update(Note note) throws SQLException {
        note.setDateCreation(System.currentTimeMillis());
        NoteCRUD.update(note);
        return note.getId();
    }

    /**
     * Delete a note in the database
     *
     * @param id
     * @return note
     * @throws SQLException
     */
    public Note delete(int id) throws SQLException {
        Note note = read(id);
        NoteCRUD.delete(note);
        return note;
    }

    /**
     * Get a note by its id
     *
     * @param id
     * @return note
     * @throws SQLException
     */
    public Note read(int id) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", id));
        List<Note> notes = NoteCRUD.read(criterias);
        return notes.get(0);
    }



    /**
     * Get all the notes of the given entity (the average note and the count
     * note by type)
     *
     * @param entityId
     * @param entityType
     * @return
     * @throws SQLException
     */
    public NoteWS getNotes(int entityId, EntityType entityType) throws SQLException {
        NoteWS noteWS = new NoteWS();

        noteWS.setAllNotesCount(noteHelper.getNotesCountsByEntity(entityId, entityType));
        noteWS.setMoyenne(noteHelper.getMoyenneByEntity(entityId, entityType));
        noteWS.setNoteOneCounts(noteHelper.getNoteCountsByType(entityId, entityType, NoteType.UN));
        noteWS.setNoteTwoCounts(noteHelper.getNoteCountsByType(entityId, entityType, NoteType.DEUX));
        noteWS.setNoteThreeCounts(noteHelper.getNoteCountsByType(entityId, entityType, NoteType.TROIS));
        noteWS.setNoteFourCounts(noteHelper.getNoteCountsByType(entityId, entityType, NoteType.QUATRE));
        noteWS.setNoteFiveCounts(noteHelper.getNoteCountsByType(entityId, entityType, NoteType.CINQ));

        return noteWS;
    }

    /**
     * Get all the notes of the given entityType (the average note and the count
     * note by type)
     *
     * @param entityType
     * @return
     * @throws SQLException
     */
    public NoteWS getNotes(EntityType entityType) throws SQLException {
        NoteWS noteWS = new NoteWS();

        noteWS.setAllNotesCount(noteHelper.getNotesCountsByEntity(entityType));
        noteWS.setMoyenne(noteHelper.getMoyenneByEntity(entityType));
        noteWS.setNoteOneCounts(noteHelper.getNoteCountsByType(entityType, NoteType.UN));
        noteWS.setNoteTwoCounts(noteHelper.getNoteCountsByType(entityType, NoteType.DEUX));
        noteWS.setNoteThreeCounts(noteHelper.getNoteCountsByType(entityType, NoteType.TROIS));
        noteWS.setNoteFourCounts(noteHelper.getNoteCountsByType(entityType, NoteType.QUATRE));
        noteWS.setNoteFiveCounts(noteHelper.getNoteCountsByType(entityType, NoteType.CINQ));

        return noteWS;
    }
}
