package xyz.cofe.delphi.doc;

import xyz.cofe.coll.im.ImList;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Указывает, где искать исходники и как их читать
 */
public interface SearchPas {
    Stream<PasFile> stream();

    record PasFile(Path file, Charset charset) {}

    record Root(Path directory, Pattern pathPattern, Charset charset) implements SearchPas {
        public Stream<PasFile> stream() {
            Iterable<Path> iter = () -> new FileIterator(directory);
            return StreamSupport.stream(iter.spliterator(),false)
                .filter(Files::isRegularFile)
                .filter(f -> pathPattern.matcher(f.toString()).matches() )
                .map( f -> new PasFile(f, charset));
        }
    }

    record Roots(ImList<SearchPas,?> roots) implements SearchPas {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        public Stream<PasFile> stream(){
            if( roots.size()==0 )return Stream.empty();
            if( roots.size()==1 )return roots.get(0).get().stream();
            var st = roots.get(0).get().stream();
            for( var i=1;i<roots.size();i++ ){
                var itm = roots.get(i);
                if(itm.isPresent()){
                    var n = itm.get().stream();
                    st = Stream.concat(st,n);
                }
            }
            return st;
        }
    }
}
