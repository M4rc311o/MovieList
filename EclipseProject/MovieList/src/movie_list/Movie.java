package movie_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Movie {
    private String name;
    private String director;
    private int year;
    private MovieType movieType;
    private Set<String> setOfPeopleNames;
    private List<Review> listOfReviews;

    public Movie(String name, String directorName, int year, MovieType movieType){
        this.name = name;
        this.director = directorName;
        this.year = year;
        this.movieType = movieType;
        this.setOfPeopleNames = new HashSet<String>();
        this.listOfReviews = new ArrayList<Review>();
    }

    public Movie(String name, String directorName, int year, MovieType movieType, Set<String> people){
        this.name = name;
        this.director = directorName;
        this.year = year;
        this.movieType = movieType;
        this.setOfPeopleNames = people;
        this.listOfReviews = new ArrayList<Review>();
    }

    public Movie(String name, String directorName, int year, MovieType movieType, Set<String> people, List<Review> reviews){
        this.name = name;
        this.director = directorName;
        this.year = year;
        this.movieType = movieType;
        this.setOfPeopleNames = people;
        this.listOfReviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String movieName) {
        this.name = movieName;
    }

    public String getDirectorName() {
        return director;
    }

    public void setDirectorName(String directorName) {
        this.director = directorName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public MovieType getMovieType() {
        return movieType;
    }

    public boolean addPerson(String personName){
        if(setOfPeopleNames.contains(personName)) return false;
        return setOfPeopleNames.add(personName);
    }

    public boolean deletePerson(String actorName){
        if(!setOfPeopleNames.contains(actorName)) return false;
        return setOfPeopleNames.remove(actorName);
    }

    public Set<String> getSetOfPersonNames() {
        return setOfPeopleNames;
    }

    public void addReview(Review review){
        listOfReviews.add(review);
    }

    public List<Review> getListOfReviews(){
        Collections.sort(listOfReviews, Collections.reverseOrder());
        return listOfReviews;
    }
}
