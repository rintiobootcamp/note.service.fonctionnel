package com.bootcamp.service;

import com.bootcamp.application.Application;
import com.bootcamp.commons.enums.NoteType;
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.crud.NoteCRUD;
import com.bootcamp.entities.Note;
import com.bootcamp.entities.Projet;
import com.bootcamp.services.NoteService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ibrahim on 12/9/17.
 */

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = NoteService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(NoteCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class NoteServiceTest {
    private final Logger LOG = LoggerFactory.getLogger(NoteServiceTest.class);

    @InjectMocks
    private NoteService noteService;


    @Test
    public void getByCriterias() throws Exception {
        int entityId = 2;
        String entityType ="PROJET";
        List<Note> notes = getNotes(entityType,entityId);
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), null));
        PowerMockito.mockStatic(NoteCRUD.class);
        Mockito.
                when(NoteCRUD.read(criterias)).thenReturn(notes);
    }

    @Test
    public void countNoteByCriterias() throws Exception {
        int entityId = 2;
        String entityType ="PROJET";
        NoteType noteType = NoteType.DEUX;
        List<Note> notes = getNotes(entityType,entityId);
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", entityId), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", entityType), "AND"));
        criterias.addCriteria(new Criteria(new Rule("noteType", "=", noteType), null));
        PowerMockito.mockStatic(NoteCRUD.class);
        Mockito.
                when(NoteCRUD.read(criterias)).thenReturn(notes);
    }

    @Test
    public void create() throws Exception{
        List<Note> notes = loadDataNoteFromJsonFile();
        Note note = notes.get(1);

        PowerMockito.mockStatic(NoteCRUD.class);
        Mockito.
                when(NoteCRUD.create(note)).thenReturn(true);
    }

    @Test
    public void getByCriteria() throws Exception{
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("entityType", "=", "PROJET"), "AND"));
        criterias.addCriteria(new Criteria(new Rule("entityId", "=", 2), null));
        List<Note> noteList = getNotes("SECTEUR",2);

        PowerMockito.mockStatic(NoteCRUD.class);
        Mockito.
                when(NoteCRUD.read(criterias)).thenReturn(noteList);
        Gson gson = new Gson();
        for(Note current:noteList){
            System.out.println(gson.toJson(current));
        }
    }

    @Test
    public void delete() throws Exception{
        List<Note> notes = loadDataNoteFromJsonFile();
        Note note = notes.get(1);

        PowerMockito.mockStatic(NoteCRUD.class);
        Mockito.
                when(NoteCRUD.delete(note)).thenReturn(true);
    }

    @Test
    public void update() throws Exception{
        List<Note> notes = loadDataNoteFromJsonFile();
        Note note = notes.get(1);

        PowerMockito.mockStatic(NoteCRUD.class);
        Mockito.
                when(NoteCRUD.update(note)).thenReturn(true);
    }

    private List<Note> getNotes(String entityType,int entityId) throws Exception {
        List<Note> notes = loadDataNoteFromJsonFile();
        List<Note> result = notes.stream().filter(item -> item.getEntityType().equals(entityType) && item.getEntityId()==entityId).collect(Collectors.toList());

        return result;
    }

    private List<Note> getCountNotes(String entityType,int entityId,NoteType noteType) throws Exception {
        List<Note> notes = loadDataNoteFromJsonFile();
        List<Note> result = notes.stream().filter(item -> item.getEntityType().equals(entityType) && item.getEntityId()==entityId && item.getNoteType().equals(noteType)).collect(Collectors.toList());

        return result;
    }


    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    public List<Note> loadDataNoteFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "notes.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<Note>>() {
        }.getType();
        List<Note> notes = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return notes;
    }


    public Note getNote(int id) throws Exception {
        List<Note> notes = loadDataNoteFromJsonFile();
        Note note = notes.stream().filter(item -> item.getId() == id).findFirst().get();
        LOG.info(note.toString());

        return note;
    }

}