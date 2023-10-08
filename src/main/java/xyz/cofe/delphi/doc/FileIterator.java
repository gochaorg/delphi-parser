package xyz.cofe.delphi.doc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class FileIterator implements Iterator<Path> {
    public FileIterator(Iterable<Path> roots){
        if( roots==null ) throw new IllegalArgumentException("roots==null");
        roots.forEach(workSet::add);
    }

    public FileIterator(Path ... roots){
        if( roots==null ) throw new IllegalArgumentException("roots==null");
        workSet.addAll(List.of(roots));
    }

    protected List<Path> workSet = new ArrayList<>();

    @Override
    public boolean hasNext() {
        return !workSet.isEmpty();
    }

    @Override
    public Path next() {
        if(workSet.isEmpty())throw new NoSuchElementException();
        var f = workSet.remove(0);
        if( Files.isDirectory(f) ){
            try( var ds = Files.newDirectoryStream(f) ) {
                var sub = new ArrayList<Path>();
                ds.forEach(sub::add);
                workSet.addAll(0,sub);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return f;
    }
}
