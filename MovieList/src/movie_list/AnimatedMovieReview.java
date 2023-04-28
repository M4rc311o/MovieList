package movie_list;
public class AnimatedMovieReview extends Review{
    public AnimatedMovieReview(int rating, String reviewText){
        super(rating, reviewText);
        if(rating < 1 || rating > 10) throw new IllegalArgumentException("Number out of range");
    }

    public AnimatedMovieReview(int rating){
        super(rating);
        if(rating < 1 || rating > 10) throw new IllegalArgumentException("Number out of range");
    }
}
