package com.bootcamp.services;

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

    /**
     * Insert a note in the database
     *
     * @param note
     * @return the note id
     * @throws SQLException
     */
    public int create(Note note) throws SQLException {
        note.setDateCreation(System.currentTimeMillis());
        NoteCRUD.create(note);
        return note.getId();
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
     * Get the average note of the given entity
     *
     * @param entityId
     * @param entityType
     * @return
     * @throws SQLException
     */
    public double getMoyenneByEntity(int entityId, EntityType entityType) throws SQLException {
        double moyenne = 0;
        int count = 0;
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        List<Note> notes = NoteCRUD.read(criterias);
        for (Note note : notes) {
            NoteType noteType = NoteType.valueOf(note.getNoteType());
            int n = noteType.ordinal() + 1;
            moyenne += n;
            count++;
        }
        return moyenne / count;
    }

    /**
     * Get the notes count of the given note type and entity
     *
     * @param entityId
     * @param entityType
     * @param noteType
     * @return
     * @throws SQLException
     */
    public int getNoteCounts(int entityId, EntityType entityType, NoteType noteType) throws SQLException {
        int count = 0;
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
        criterias.addCriteria(new Criteria(new Rule("noteType", "=", noteType), null));
        count = NoteCRUD.read(criterias).size();
        return count;
    }

    /**
     * Get the count of all the notes of a given entity
     *
     * @param noteType
     * @param entityType
     * @return count
     * @throws SQLException
     */
    public int getNotesCountsByEntity(NoteType noteType, EntityType entityType) throws SQLException {
        int count = 0;
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("noteType", "=", noteType), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        count = NoteCRUD.read(criterias).size();
        return count;
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

        noteWS.setEntityId(entityId);
        noteWS.setEntityType(entityType);
        noteWS.setMoyenne(this.getMoyenneByEntity(entityId, entityType));
        noteWS.setNoteOneCounts(this.getNoteCounts(entityId, entityType, NoteType.UN));
        noteWS.setNoteTwoCounts(this.getNoteCounts(entityId, entityType, NoteType.DEUX));
        noteWS.setNoteThreeCounts(this.getNoteCounts(entityId, entityType, NoteType.TROIS));
        noteWS.setNoteFourCounts(this.getNoteCounts(entityId, entityType, NoteType.QUATRE));
        noteWS.setNoteFiveCounts(this.getNoteCounts(entityId, entityType, NoteType.CINQ));

        return noteWS;
    }
}
