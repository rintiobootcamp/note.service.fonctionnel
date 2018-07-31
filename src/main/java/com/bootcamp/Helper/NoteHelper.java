package com.bootcamp.Helper;

import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.enums.NoteType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.NoteCRUD;
import com.bootcamp.entities.Note;
import com.bootcamp.services.NoteService;
import com.rintio.elastic.client.ElasticClient;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoteHelper {
    ElasticClient elasticClient ;
    private List<Note> notes;
    @PostConstruct
    public void init(){
        this.notes = new ArrayList<>();
        elasticClient = new ElasticClient();
    }


    public List<Note> lireNote() throws Exception{
        if(this.notes.isEmpty())
            getAllNote();
        return this.notes;
    }

    public List<Note> getAllNote() throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Object> objects = elasticClient.getAllObject("notes");
        ModelMapper modelMapper = new ModelMapper();
        List<Note> rest = new ArrayList<>();
        for(Object obj:objects){
            rest.add(modelMapper.map(obj,Note.class));
        }
        this.notes = rest;
        return rest;
    }
    public boolean createAllIndexNote()throws Exception{
        List<Note> notes = NoteCRUD.read();
        for (Note note : notes){
            elasticClient.creerIndexObjectNative("notes","note",note,note.getId());
        }
        return true;
    }

    /**
     * Get the average note of the given entity
     *
     * @param entityId
     * @param entityType
     * @return
     * @throws SQLException
     */
    public double getMoyenneByEntity(int entityId, EntityType entityType) throws Exception {
        double moyenne = 0;
        int count = 0;
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
//        List<Note> notes = NoteCRUD.read(criterias);
        List<Note> notes = lireNote().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString()) && t.getEntityId()==entityId).collect(Collectors.toList());
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
    public int getNoteCountsByType(int entityId, EntityType entityType, NoteType noteType) throws Exception {
        int count = 0;
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("noteType", "=", noteType), null));
//        count = NoteCRUD.read(criterias).size();
        count = lireNote().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString())).collect(Collectors.toList())
                .stream().filter(o->o.getNoteType().equalsIgnoreCase(noteType.toString())).collect(Collectors.toList())
                .stream().filter(u->u.getEntityId()==entityId).collect(Collectors.toList()).size();
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
    public int getNotesCountsByEntity(int entityId, EntityType entityType) throws Exception {
        int count = 0;
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        count = lireNote().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString())).collect(Collectors.toList())
                .stream().filter(u->u.getEntityId()==entityId).collect(Collectors.toList()).size();
        return count;
    }

    /**
     * Get the average note of the given entityType
     *
     * @param entityType
     * @return
     * @throws SQLException
     */
    public double getMoyenneByEntity(EntityType entityType) throws Exception {
        double moyenne = 0;
        int count = 0;
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        List<Note> notes = lireNote().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString())).collect(Collectors.toList());
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
    public int getNoteCountsByType(EntityType entityType, NoteType noteType) throws Exception {
        int count = 0;
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
//        criterias.addCriteria(new Criteria(new Rule("noteType", "=", noteType), null));
        count = (int) lireNote().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString())).collect(Collectors.toList())
                .stream().filter(r->r.getNoteType().equalsIgnoreCase(noteType.toString())).collect(Collectors.toList()).size();
        return count;
    }

    /**
     * Get the count of all the notes of a given entityType
     *
     * @param entityType
     * @return count
     * @throws SQLException
     */
    public int getNotesCountsByEntity(EntityType entityType) throws Exception {
        int count = 0;
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        count = (int)lireNote().stream().filter(t->t.getEntityType().equalsIgnoreCase(entityType.toString())).count();
        return count;
    }
}
