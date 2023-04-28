package movie_list;
public class FeatureMovieReview extends Review {
    public FeatureMovieReview(int rating, String reviewText){
        super(rating, reviewText);
        if(rating < 1 || rating > 5) throw new IllegalArgumentException("Number out of range");
    }

    public FeatureMovieReview(int rating){
        super(rating);
        if(rating < 1 || rating > 5) throw new IllegalArgumentException("Number out of range");
    }
}
