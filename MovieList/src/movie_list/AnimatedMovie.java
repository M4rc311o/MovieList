package movie_list;
import java.util.List;
import java.util.Set;

public class AnimatedMovie extends Movie{
    private int recommendedAge;

    public AnimatedMovie(String name, String directorName, int year, int recommendedAge){
        super(name, directorName, year, MovieType.ANIMATED);
        this.recommendedAge = recommendedAge;
    }

    public AnimatedMovie(String name, String directorName, int year, int recommendedAge, Set<String> animators){
        super(name, directorName, year, MovieType.ANIMATED, animators);
        this.recommendedAge = recommendedAge;
    }

    public AnimatedMovie(String name, String directorName, int year, int recommendedAge, Set<String> animators, List<Review> reviews){
        super(name, directorName, year, MovieType.ANIMATED, animators, reviews);
        this.recommendedAge = recommendedAge;
    }

    public int getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(int recommendedAge) {
        this.recommendedAge = recommendedAge;
    }
}
