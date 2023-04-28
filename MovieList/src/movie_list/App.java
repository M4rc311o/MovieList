package movie_list;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        int choice;

        database.loadFromDatabase();

        while(run){
            System.out.println("\nVyberte akci:\n-------------");
            System.out.println("1 -> Zobrazit vsechny filmy");
            System.out.println("2 -> Vlozit novy film");
            System.out.println("3 -> Upravit film");
            System.out.println("4 -> Smazat film");
            System.out.println("5 -> Zobrazit film");
            System.out.println("6 -> Exportovat film");
            System.out.println("7 -> Importovat film");
            System.out.println("8 -> Zobrazit osobu");
            System.out.println("9 -> Pridat recenzi filmu");
            System.out.println("10 -> Zobrazit vsechny osoby s vice nez jednim filmem");
            System.out.println("11 -> Ukoncit aplikaci");
            choice = ConsoleInteractions.readInteger(sc);

            switch (choice) {
                case 1:
                    Iterator<Movie> movieIterator = database.getMoviesIterable().iterator();
                    while(movieIterator.hasNext()) ConsoleInteractions.printMovie(movieIterator.next());
                    break;
                case 2:
                    MovieType movieType = ConsoleInteractions.readMovieType(sc);
                    String movieName = ConsoleInteractions.readMovieName(sc);
                    if(database.checkIfMovieExist(movieName)){
                        System.out.println("Film s timto jmenem je jiz v databazi ulozen");
                        break;
                    }
                    int movieYear = ConsoleInteractions.readMovieYear(sc);
                    String directorName = ConsoleInteractions.readMovieDirector(sc);
                    Set<String> peopleSet = null;

                    switch (movieType){
                        case FEATURE:
                            peopleSet = ConsoleInteractions.readMovieActors(sc);
                            database.insertMovie(new FeatureMovie(movieName, directorName, movieYear, peopleSet));
                            break;
                        case ANIMATED:
                            peopleSet = ConsoleInteractions.readMovieAnimators(sc);
                            int recommendedAge = ConsoleInteractions.readMovieRecommendedAge(sc);
                            database.insertMovie(new AnimatedMovie(movieName, directorName, movieYear, recommendedAge, peopleSet));
                            break;
                    }
                    break;       
                case 3:
                    editMovie(database, sc);
                    break;
                case 4:
                    movieName = ConsoleInteractions.readMovieName(sc);
                    if(!database.checkIfMovieExist(movieName)) System.out.println("Takovy film v databazi neni");
                    else database.removeMovie(movieName);
                    break;
                case 5:
                    Movie movie = database.searchMovie(ConsoleInteractions.readMovieName(sc));
                    if(movie == null) System.out.println("Takovy film v databazi neni");
                    else ConsoleInteractions.printMovie(movie);
                    break;
                case 6:
                    movieName = ConsoleInteractions.readMovieName(sc);
                    if(!database.checkIfMovieExist(movieName)) System.out.println("Takovy film v databazi neni");
                    else database.exportMovieToFile(movieName);
                    break;
                case 7:
                    String filePath = ConsoleInteractions.readFilePath(sc);
                    database.importMovieFromFile(filePath);
                    break;
                case 8:
                    Person person = database.searchPerson(ConsoleInteractions.readPersonName(sc));
                    if(person == null) System.out.println("Nikdo takovy v databazi neni");
                    else ConsoleInteractions.printPerson(person);
                    break;
                case 9:
                    movieName = ConsoleInteractions.readMovieName(sc);
                    if(!database.checkIfMovieExist(movieName)){
                        System.out.println("Takovy film v databazi neni");
                        break;
                    }
                    Review review = ConsoleInteractions.readMovieReview(database.searchMovie(movieName).getMovieType(), sc);
                    database.addMovieReview(movieName, review);
                    break;
                case 10:
                    Iterator<Person> personIterator = database.getPeopleIterable().iterator();
                    while(personIterator.hasNext()){
                        Person personP = personIterator.next();
                        if(personP.getListOfMovies(PersonProfession.DIRECTOR).size() < 2 && personP.getListOfMovies(PersonProfession.ACTOR).size() < 2 && personP.getListOfMovies(PersonProfession.ANIMATOR).size() < 2) continue;
                        ConsoleInteractions.printPerson(personP);
                    } 
                    break;

                case 11:
                    sc.close();
                    database.saveToDatabase();
                    DBConnection.closeDBConnection();
                    run = false;
                    break;

                default:
                    System.out.println("Zadejte platnou hodnotu volby");
                    break;
            }
        }
    }

    public static void editMovie(Database database, Scanner sc){
        boolean run = true;
        Movie movie = database.searchMovie(ConsoleInteractions.readMovieName(sc)); 
        if(movie == null) {
            System.out.println("Takovy film v databzi neni");
            return;
        }
        while(run){
            System.out.println("\nAktulani data k filmu:\n----------------------");
            ConsoleInteractions.printMovie(movie);
            System.out.println("\nJakou informaci chcete upravit:");
            System.out.println("1 -> Jmeno");
            System.out.println("2 -> Reziser");
            System.out.println("3 -> Rok vydani");
            if(movie instanceof FeatureMovie){
                System.out.println("4 -> List hercu");
                System.out.println("5 -> Odejit");
            }
            else if(movie instanceof AnimatedMovie){
                System.out.println("4 -> List animatoru");
                System.out.println("5 -> Doporuceny vek");
                System.out.println("6 -> Odejit");
            }
            int choice = ConsoleInteractions.readInteger(sc);

            switch(choice){
                case 1:
                    String newMovieName = ConsoleInteractions.readMovieName(sc);
                    if(database.checkIfMovieExist(newMovieName)) System.out.println("Film s timto jmenem je jiz v databazi ulozen");
                    else movie = database.editMovieName(movie.getName(), newMovieName);
                    break;

                case 2:
                    String directorName = ConsoleInteractions.readPersonName(sc);
                    database.editMovieDirector(movie.getName(), directorName);
                    break;

                case 3:
                    database.editMovieYear(movie.getName(), ConsoleInteractions.readMovieYear(sc));
                    break;
                
                case 4:
                    String personName = ConsoleInteractions.readPersonName(sc);
                    database.editMoviePerson(movie.getName(), personName);
                    break;

                case 5:
                    if(movie instanceof FeatureMovie) {
                        run = false;
                        break;
                    }
                    database.editAnimatedMovieRecommendedAge(movie.getName(), ConsoleInteractions.readMovieRecommendedAge(sc));
                    break;

                case 6:
                    if(movie instanceof FeatureMovie){
                        System.out.println("Zadejte platnou hodnotu volby");
                        break;
                    }
                    run = false;
                    break;

                default:
                    System.out.println("Zadejte platnou hodnotu volby");
                    break;
            }
        }
    }
}
