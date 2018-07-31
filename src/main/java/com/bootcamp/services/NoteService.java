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
import com.bootcamp.entities.Media;
import com.bootcamp.entities.Note;
//import org.eclipse.persistence.internal.helper.Helper;
import com.rintio.elastic.client.ElasticClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darextossa on 11/27/17.
 */
@Component
public class NoteService implements DatabaseConstants {

    NoteHelper noteHelper = new NoteHelper();
    ElasticClient elasticClient ;
    private List<Note> notes;

    @PostConstruct
    public void init(){
        this.notes = new ArrayList<>();
        elasticClient = new ElasticClient();
    }

    public List<Note> lireNote() throws Exception{
        if(this.notes.isEmpty())
            getAllNoteIndex();
        return this.notes;
    }

    /**
     * Insert a note in the database
     *
     * @param note
     * @return the note id
     * @throws SQLException
     */
    public Note create(Note note) throws Exception {
        note.setDateCreation(System.currentTimeMillis());
        NoteCRUD.create(note);
        createAllIndexNote();
        return note;
    }

    /**
     * Update a note in the database
     *
     * @param note
     * @return id
     * @throws SQLException
     */
    public int update(Note note) throws Exception {
        note.setDateCreation(System.currentTimeMillis());
        NoteCRUD.update(note);
        createAllIndexNote();
        return note.getId();
    }

    /**
     * Delete a note in the database
     *
     * @param id
     * @return note
     * @throws SQLException
     */
    public Note delete(int id) throws Exception {
        Note note = read(id);
        NoteCRUD.delete(note);
        createAllIndexNote();
        return note;
    }

    /**
     * Get a note by its id
     *
     * @param id
     * @return note
     * @throws SQLException
     */
    public Note read(int id) throws Exception {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria("id", "=", id));
//        List<Note> notes = NoteCRUD.read(criterias);
//        return notes.get(0);
        return lireNote().stream().filter(t->t.getId()==id).findFirst().get();
    }

    public List<Note> getAllNoteIndex() throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Object> objects = elasticClient.getAllObject("notes");
        ModelMapper modelMapper = new ModelMapper();
        List<Note> rest = new ArrayList<>();
        for(Object obj:objects){
            rest.add(modelMapper.map(obj,Note.class));
        }
        this.notes= rest;
        return rest;
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
    public NoteWS getNotes(int entityId, EntityType entityType) throws Exception {
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
    public NoteWS getNotes(EntityType entityType) throws Exception {
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


//    public List<Note> getAllNote() throws Exception{
//        ElasticClient elasticClient = new ElasticClient();
//        List<Object> objects = elasticClient.getAllObject("notes");
//        ModelMapper modelMapper = new ModelMapper();
//        List<Note> rest = new ArrayList<>();
//        for(Object obj:objects){
//            rest.add(modelMapper.map(obj,Note.class));
//        }
//        return rest;
//    }
    public boolean createAllIndexNote()throws Exception{
        List<Note> notes = NoteCRUD.read();
        for (Note note : notes){
            elasticClient.creerIndexObjectNative("notes","note",note,note.getId());
        }
        return true;
    }
}
