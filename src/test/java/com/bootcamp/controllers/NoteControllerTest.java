package com.bootcamp.controllers;

import com.bootcamp.application.Application;
import com.bootcamp.commons.enums.EntityType;
import com.bootcamp.commons.enums.NoteType;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.commons.ws.usecases.pivotone.NoteWS;
import com.bootcamp.entities.Note;
import com.bootcamp.services.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 *
 * Created by Ibrahim on 12/5/17.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = NoteController.class, secure = false)
@ContextConfiguration(classes={Application.class})
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NoteService noteService;



    @Test
    public void readByEntityTest() throws Exception{
        NoteWS noteWS = new NoteWS();
        noteWS.setNoteFourCounts(0);
        noteWS.setEntityId(7);
        noteWS.setEntityType(EntityType.PROJET);
        noteWS.setNoteFiveCounts(0);
        noteWS.setNoteThreeCounts(0);
        noteWS.setNoteOneCounts(3);
        noteWS.setNoteTwoCounts(0);
        noteWS.setMoyenne(3/5.0);
        EntityType pro = EntityType.PROJET;
        int id = 7;

        Mockito.
        when(noteService.getNotes(7,EntityType.PROJET)).thenReturn(noteWS);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/notes/{entityType}/{entityId}",pro,id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

    }

   /* @Test
    public void getNoteById() throws Exception{
        int id = 1;
        Note note = getNote(id);

        when(noteService.read(id)).thenReturn(note);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/notes/{id}",id)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for get a note by id in note controller done *******************");

    }
*/
    @Test
    public void CreateNoteTest() throws Exception{
        Note note = getNote(2);

        when(noteService.create(note)).thenReturn(note);

        RequestBuilder requestBuilder =
                post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(note));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        System.out.println(response.getContentAsString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
        System.out.println("*********************************Test for create note in note controller done *******************");

    }


        private int getCountNotes(String entityType,int entityId,NoteType noteType) throws Exception {
        List<Note> notes = loadDataNoteFromJsonFile();
        List<Note> result = notes.stream().filter(item -> item.getEntityType().equals(entityType) && item.getEntityId()==entityId && item.getNoteType().equals(noteType)).collect(Collectors.toList());

        return result.size();
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

        return note;
    }



    public static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if(!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

}