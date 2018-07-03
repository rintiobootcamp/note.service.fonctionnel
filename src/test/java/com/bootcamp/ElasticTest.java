package com.bootcamp;

import com.bootcamp.crud.MediaCRUD;
import com.bootcamp.crud.NoteCRUD;
import com.bootcamp.entities.Media;
import com.bootcamp.entities.Note;
import com.rintio.elastic.client.ElasticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

public class ElasticTest {
    private final Logger LOG = LoggerFactory.getLogger(ElasticTest.class);


    @Test
    public void createIndexNote()throws Exception{
        ElasticClient elasticClient = new ElasticClient();
        List<Note> notes = NoteCRUD.read();
        for (Note note : notes){
            elasticClient.creerIndexObjectNative("notes","note",note,note.getId());
            LOG.info("note "+note.getId()+" created");
        }
    }
}
