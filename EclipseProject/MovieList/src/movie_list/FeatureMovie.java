package movie_list;
import java.util.List;
import java.util.Set;

public class FeatureMovie extends Movie {
    public FeatureMovie(String name, String directorName, int year){
        super(name, directorName, year, MovieType.FEATURE);
    }

    public FeatureMovie(String name, String directorName, int year, Set<String> actors){
        super(name, directorName, year, MovieType.FEATURE, actors);
    }

    public FeatureMovie(String name, String directorName, int year, Set<String> actors, List<Review> reviews){
        super(name, directorName, year, MovieType.FEATURE, actors, reviews);
    }
}
