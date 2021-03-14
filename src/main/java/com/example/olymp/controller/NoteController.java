package com.example.olymp.controller;

import com.example.olymp.model.Notes;
import com.example.olymp.model.NotesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@RestController
public class NoteController {
    private List<Notes> listNotes = new ArrayList<>();
    private static Long countNotest = 0L;
    @Autowired
    @Qualifier(value = "myPropertyBean")
    private Properties properties;

    @PostMapping("/notes")
    public Notes saveNotes(@RequestBody NotesDto notes) {
        Notes notes1 = toNotes(notes);
        listNotes.add(notes1);
        return notes1;
    }
  /*  @GetMapping("/notes")
    public List<Notes> notes() {
        List<Notes> notes = listNotes
                .stream()
                .filter(x-> x.getTitle()==null)
                .map(this::titleIsEmpty)
                .collect(Collectors.toList());
        return notes;
    }*/
    @GetMapping("/notes/{id}")
    public Notes notes( @PathVariable Long id) {
       List<Notes> notesList = listNotes.stream()
               .filter(n -> n.getId().equals(id))
               .collect(Collectors.toList());
        List<Notes> notes1 = notesList
                .stream()
                .filter(x-> x.getTitle()==null)
                .map(this::titleIsEmpty)
                .collect(Collectors.toList());
        if(!notes1.isEmpty()) {
            Notes notes = notes1.get(0);
            return notes;
        }
        else {
            Notes notes = notesList.get(0);
            return notes;
        }
    }
    @PutMapping("/notes/{id}")
    public void notesChange(@RequestBody NotesDto notes, @PathVariable Long id) {
        int index = 0;
        for (Notes notes1: listNotes) {
            if(notes1.getId().equals(id)) {
                break;
            }
            else ++index;
        }
        List<Notes> notesList = listNotes.stream()
                .filter(n -> n.getId().equals(id))
                .collect(Collectors.toList());
        Notes tempNotes = notesList.get(0);

        Notes notes1 = Notes.builder()
                .id(tempNotes.getId())
                .content(notes.getContent())
                .title(notes.getTitle())
                .build();
        listNotes.set(index,notes1);
    }

    @GetMapping("/notes")
    public List<Notes> findNotes(@RequestParam(value = "query", required = false) String query) {
        if(query==null) {
            List<Notes> notes = listNotes
                    .stream()
                    .filter(x-> x.getTitle()==null)
                    .map(this::titleIsEmpty)
                    .collect(Collectors.toList());
            if(notes.isEmpty()) {
                return listNotes;
            }
            else return notes;
        } else {
            List<Notes> list = listNotes
                    .stream()
                    .filter(e -> e.getTitle().contains(query))
                    .collect(Collectors.toList());
            List<Notes> list1 = listNotes
                    .stream()
                    .filter(e -> e.getContent().contains(query))
                    .collect(Collectors.toList());
            if (!list.isEmpty() && !list1.isEmpty()) {
                List<Notes> list2 = new ArrayList<>();
                list2.addAll(list);
                list2.addAll(list1);
                return list2;
            } else if (!list.isEmpty()) {
                return list;
            } else return list1;
        }
    }
    @DeleteMapping("/notes/{id}")
    public void deleteNotes( @PathVariable Long id) {
        int index = 0;
        for (Notes notes1: listNotes) {
            if(notes1.getId().equals(id)) {
                break;
            }
            else ++index;
        }
        listNotes.remove(index);
    }

    private Notes titleIsEmpty(Notes notes) {

        int begin = Integer.parseInt( properties.getProperty("begin"));
        int end = Integer.parseInt(properties.getProperty("end"));

        return Notes.builder()
                .content(notes.getContent())
                .title(notes.getContent().substring(begin,end))
                .build();
    }

    private Notes toNotes(NotesDto notesDto) {
        return Notes.builder()
                .content(notesDto.getContent())
                .id(++countNotest)
                .title(notesDto.getTitle())
                .build();
    }
}
