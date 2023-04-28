package movie_list;
public class Review implements Comparable<Review>{
    private int rating;
    private String reviewText;

    public Review(int rating, String reviewText){
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public Review(int rating){
        this.rating = rating;
        this.reviewText = null;
    }

    public int getRating(){
        return rating;
    }

    public String getReviewText(){
        return reviewText;
    }

    @Override
    public int compareTo(Review review){
        return this.rating - review.getRating();
    }
}
