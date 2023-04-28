package movie_list;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MovieData implements Data<String, Movie>, Iterable<Movie> {
    private HashMap<String, Movie> movieDatabase;

    public MovieData(){
        movieDatabase = new HashMap<String, Movie>();
    }

    public boolean add(Movie movie){
        if(movieDatabase.containsKey(movie.getName())) return false;
        movieDatabase.put(movie.getName(), movie);
        return true;
    }

    public boolean delete(String movieName){
        if(movieDatabase.remove(movieName) == null) return false;
        return true;
    }

    public Movie search(String movieName){
        return movieDatabase.get(movieName);
    }

    public boolean checkIfExist(String movieName){
        return movieDatabase.containsKey(movieName);
    }

    @Override
    public Iterator<Movie> iterator(){
        Iterator<Movie> iterator;
        iterator = new Iterator<Movie>(){
            Iterator<Map.Entry<String, Movie>> mapIterator = movieDatabase.entrySet().iterator();

            @Override
            public boolean hasNext(){
                return mapIterator.hasNext();
            }

            @Override
            public Movie next(){
                return mapIterator.next().getValue();
            }
        };
        return iterator;
    }
}
