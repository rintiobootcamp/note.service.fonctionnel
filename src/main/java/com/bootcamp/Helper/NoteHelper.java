package com.bootcamp.Helper;

import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.enums.NoteType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.NoteCRUD;
import com.bootcamp.entities.Note;

import java.sql.SQLException;
import java.util.List;

public class NoteHelper {

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

        if (count==0){
            return 0;
        }
        else {
            return moyenne / count;
        }

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
    public int getNoteCountsByType(int entityId, EntityType entityType, NoteType noteType) throws SQLException {
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
     * @param entityId
     * @param entityType
     * @return count
     * @throws SQLException
     */
    public int getNotesCountsByEntity(int entityId, EntityType entityType) throws SQLException {
        int count = 0;
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        count = NoteCRUD.read(criterias).size();
        return count;
    }

    /**
     * Get the average note of the given entityType
     *
     * @param entityType
     * @return
     * @throws SQLException
     */
    public double getMoyenneByEntity(EntityType entityType) throws SQLException {
        double moyenne = 0;
        int count = 0;
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        List<Note> notes = NoteCRUD.read(criterias);
        for (Note note : notes) {
            NoteType noteType = NoteType.valueOf(note.getNoteType());
            int n = noteType.ordinal() + 1;
            moyenne += n;
            count++;
        }
        if (count==0){
            return 0;
        }
        else {
            return moyenne / count;
        }
    }

    /**
     * Get the notes count of the given note type and entityType
     *
     * @param entityType
     * @param noteType
     * @return
     * @throws SQLException
     */
    public int getNoteCountsByType(EntityType entityType, NoteType noteType) throws SQLException {
        int count = 0;
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
        criterias.addCriteria(new Criteria(new Rule("noteType", "=", noteType), null));
        count = NoteCRUD.read(criterias).size();
        return count;
    }

    /**
     * Get the count of all the notes of a given entityType
     *
     * @param entityType
     * @return count
     * @throws SQLException
     */
    public int getNotesCountsByEntity(EntityType entityType) throws SQLException {
        int count = 0;
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        count = NoteCRUD.read(criterias).size();
        return count;
    }
}
