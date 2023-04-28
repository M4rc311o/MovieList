package movie_list;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Database {
    private static final Database database = new Database();
    MovieData movies = new MovieData();
    PersonData people = new PersonData();

    private Database(){}

    public static Database getInstance(){
        return database;
    }

    public boolean checkIfMovieExist(String movieName){
        return movies.checkIfExist(movieName);
    }

    public void insertMovie(Movie movie){
        if(movie == null) throw new NullPointerException();
        PersonProfession personProfession;
        if(movie.getMovieType().equals(MovieType.FEATURE)) personProfession = PersonProfession.ACTOR;
        else personProfession = PersonProfession.ANIMATOR;
        for(String personName : movie.getSetOfPersonNames()){
            if(people.checkIfExist(personName)) people.search(personName).addMovie(personProfession, movie.getName());
            else people.add(new Person(personName, personProfession, movie.getName()));
        }
        if(people.checkIfExist(movie.getDirectorName())) people.search(movie.getDirectorName()).addMovie(PersonProfession.DIRECTOR, movie.getName());
        else people.add(new Person(movie.getDirectorName(), PersonProfession.DIRECTOR, movie.getName()));
        movies.add(movie);
    }

    public boolean removeMovie(String movieName){
        Movie movie = movies.search(movieName);
        if(movie == null) return false;
        people.search(movie.getDirectorName()).deleteMovie(PersonProfession.DIRECTOR, movie.getName());
        if(movie.getMovieType().equals(MovieType.ANIMATED)) for(String animatorName : movie.getSetOfPersonNames())  people.search(animatorName).deleteMovie(PersonProfession.ANIMATOR, movie.getName());
        else for(String actorName : movie.getSetOfPersonNames()) people.search(actorName).deleteMovie(PersonProfession.ACTOR, movie.getName());
        movies.delete(movie.getName());
        return true;
    }

    public Movie searchMovie(String movieName){
        return movies.search(movieName);
    }

    public Person searchPerson(String personName){
        return people.search(personName);
    }

    public Movie editMovieName(String oldMovieName, String newMovieName){
        Movie movie = movies.search(oldMovieName);
        if(movie == null) return null;
        people.search(movie.getDirectorName()).deleteMovie(PersonProfession.DIRECTOR, oldMovieName);
        people.search(movie.getDirectorName()).addMovie(PersonProfession.DIRECTOR, newMovieName);
        for(String personName : movie.getSetOfPersonNames()){
            Person person = people.search(personName);
            person.deleteMovie(movie.getMovieType().equals(MovieType.FEATURE) ? PersonProfession.ACTOR : PersonProfession.ANIMATOR, oldMovieName);
            person.addMovie(movie.getMovieType().equals(MovieType.FEATURE) ? PersonProfession.ACTOR : PersonProfession.ANIMATOR, newMovieName);
        }
        movies.delete(oldMovieName);
        movie.setName(newMovieName);
        movies.add(movie);
        return movie;
    }

    public Movie editMovieDirector(String movieName, String newDirectorName){
        Movie movie = movies.search(movieName);
        if(movie == null) return null;
        people.search(movie.getDirectorName()).deleteMovie(PersonProfession.DIRECTOR, movieName);
        if(people.checkIfExist(newDirectorName)) people.search(newDirectorName).addMovie(PersonProfession.DIRECTOR, movieName);
        else people.add(new Person(newDirectorName, PersonProfession.DIRECTOR, movieName));
        movie.setDirectorName(newDirectorName);
        return movie;
    }

    public Movie editMovieYear(String movieName, int newYear){
        Movie movie = movies.search(movieName);
        if(movie == null) return null;
        movie.setYear(newYear);
        return movie;
    }

    public Movie editMoviePerson(String movieName, String personName){
        Movie movie = movies.search(movieName);
        if(movie == null) return null;
        if(movie.getSetOfPersonNames().contains(personName)) {
            movie.deletePerson(personName);
            people.search(personName).deleteMovie(movie.getMovieType().equals(MovieType.FEATURE) ? PersonProfession.ACTOR : PersonProfession.ANIMATOR , movie.getName());
        }
        else{
            if(people.checkIfExist(personName)) people.search(personName).addMovie(PersonProfession.ACTOR, movie.getName());
            else people.add(new Person(personName, movie.getMovieType().equals(MovieType.FEATURE) ? PersonProfession.ACTOR : PersonProfession.ANIMATOR, movie.getName()));
            movie.addPerson(personName);
        }
        return movie;
    }

    public Movie editAnimatedMovieRecommendedAge(String animatedMovieName, int newRecommendedAge){
        Movie movie = movies.search(animatedMovieName);
        if(movie == null) return null;
        ((AnimatedMovie)movie).setRecommendedAge(newRecommendedAge);
        return movie;
    }

    public Iterable<Movie> getMoviesIterable(){
        return movies;
    }

    public Iterable<Person> getPeopleIterable(){
        return people;
    }

    public void exportMovieToFile(String movieName){
        Movie movie = movies.search(movieName);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter writer = new FileWriter(movie.getName() + ".json");){
            gson.toJson(movie, writer);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void importMovieFromFile(String filePath){
        Gson gson = new Gson();
        Movie movie = null;

        try(Reader reader = new FileReader(filePath)){
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            if(json.get("movieType").getAsString().equals(MovieType.FEATURE.toString())) movie = gson.fromJson(json, FeatureMovie.class);
            else movie = gson.fromJson(json, AnimatedMovie.class);
            if(movie.getName().isEmpty() || movie.getDirectorName().isEmpty() || movie.getListOfReviews() == null) throw new Exception();
            else insertMovie(movie);
        } catch(Exception e){
            System.out.println("Chyba pri nacitani souboru");
            e.printStackTrace();
            return;
        }
    }

    public void addMovieReview(String movieName, Review review){
        Movie movie = movies.search(movieName);
        movie.addReview(review);
    }

    public void saveToDatabase(){
        DBOperations dbOperations = new DBOperations();
        dbOperations.deleteAllRecords();
        dbOperations.initialiseNewDatabase();

        Iterator<Person> peopleIterator = people.iterator();
        Person person;
        while(peopleIterator.hasNext()){
            person = peopleIterator.next();
            dbOperations.insertPerson(person.getName());
        }

        Iterator<Movie> movIterator = movies.iterator();
        Movie movie;
        while(movIterator.hasNext()){
            movie = movIterator.next();
            dbOperations.insertMovie(movie);
            dbOperations.linkPeopleToMovie(movie.getName(), movie.getSetOfPersonNames());
            dbOperations.insertMovieReviews(movie.getName(), movie.getListOfReviews());
        }
    }

    public void loadFromDatabase(){
        DBOperations dbOperations = new DBOperations();

        List<Movie> listOfMovies = dbOperations.selectAndConstructAllMovies();
        for(Movie movie : listOfMovies){
            insertMovie(movie);
        }
    }
}
