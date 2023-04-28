package movie_list;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person {
    private String name;
    private List<String> listOfDirectedMoviesNames = new ArrayList<String>();
    private List<String> listOfActedMoviesNames = new ArrayList<String>();
    private List<String> listOfAnimatedMoviesNames = new ArrayList<String>();

    public Person(String name){
        this.name = name;
    }

    public Person(String name, PersonProfession personProfession, String movieName){
        this.name = name;
        if(personProfession == PersonProfession.DIRECTOR) listOfDirectedMoviesNames.add(movieName);
        else if(personProfession == PersonProfession.ACTOR) listOfActedMoviesNames.add(movieName);
        else listOfAnimatedMoviesNames.add(movieName);
    }

    public String getName() {
        return name;
    }

    public Set<PersonProfession> getSetOfProfessions() {
        Set<PersonProfession> personProfessions = new HashSet<PersonProfession>();
        if(!listOfDirectedMoviesNames.isEmpty()) personProfessions.add(PersonProfession.DIRECTOR);
        if(!listOfActedMoviesNames.isEmpty()) personProfessions.add(PersonProfession.ACTOR);
        if(!listOfAnimatedMoviesNames.isEmpty()) personProfessions.add(PersonProfession.ANIMATOR);
        return personProfessions;
    }

    public boolean addMovie(PersonProfession personProfession, String movieName){
        if(personProfession == PersonProfession.DIRECTOR){
            if(listOfDirectedMoviesNames.contains(movieName)) return false;
            listOfDirectedMoviesNames.add(movieName);
        }
        else if(personProfession == PersonProfession.ACTOR){
            if(listOfActedMoviesNames.contains(movieName)) return false;
            listOfActedMoviesNames.add(movieName);
        }
        else{
            if(listOfAnimatedMoviesNames.contains(movieName)) return false;
            listOfAnimatedMoviesNames.add(movieName);
        }
        return true;
    }

    public boolean deleteMovie(PersonProfession personProfession, String movieName){
        if(personProfession == PersonProfession.DIRECTOR) listOfDirectedMoviesNames.remove(movieName);
        else if(personProfession == PersonProfession.ACTOR) listOfActedMoviesNames.remove(movieName);
        else listOfAnimatedMoviesNames.remove(movieName);
        return true;
    }

    public List<String> getListOfMovies(PersonProfession personProfession){
        if(personProfession == PersonProfession.DIRECTOR) return new ArrayList<String>(listOfDirectedMoviesNames);
        else if(personProfession == PersonProfession.ACTOR) return new ArrayList<String>(listOfActedMoviesNames);
        else return new ArrayList<String>(listOfAnimatedMoviesNames);
    }
}
