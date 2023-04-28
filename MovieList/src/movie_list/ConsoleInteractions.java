package movie_list;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ConsoleInteractions {
    public static MovieType readMovieType(Scanner sc){
        int temp;
        System.out.println("\nZadejte typ filmu:\n------------------\n1 -> Hrany film\n2 -> Animovany film");
        while(true){
            temp = readInteger(sc);
            if(temp == 1) return MovieType.FEATURE;
            else if(temp == 2) return MovieType.ANIMATED;
            System.out.println("Zadejte platnou hodnotu");
        }
    }

    public static String readMovieName(Scanner sc){
        System.out.println("\nZadejte jmeno filmu:");
        return sc.nextLine();
    }

    public static int readMovieYear(Scanner sc){
        System.out.println("\nZadejte rok vydani:");
        return readInteger(1800, 2100, sc);
    }

    public static String readMovieDirector(Scanner sc){
        System.out.println("\nZadejte jmeno rezisera:");
        return sc.nextLine();
    }

    public static String readPersonName(Scanner sc){
        System.out.println("\nZadejte jmeno osoby: ");
        return sc.nextLine();
    }

    public static String readFilePath(Scanner sc){
        System.out.println("\nZadejte cestu k souboru: ");
        return sc.nextLine();
    }

    public static int readMovieRecommendedAge(Scanner sc){
        System.out.println("\nZadejte doporuceny vek ke sledovani filmu:");
        return readInteger(sc);
    }

    public static Set<String> readMovieActors(Scanner sc){
        Set<String> actors = new HashSet<String>();
        System.out.println("\nZadejte cislo kolik chcete zadat hercu:");
        int actorsCount = readInteger(sc);
        for(int i = 0; i < actorsCount; i++){
            System.out.println("\nZadejte jmeno herce:");
            actors.add(sc.nextLine());
        }
        return actors;
    }

    public static Set<String> readMovieAnimators(Scanner sc){
        Set<String> actors = new HashSet<String>();
        System.out.println("\nZadejte cislo kolik chcete zadat animatoru:");
        int actorsCount = readInteger(sc);
        for(int i = 0; i < actorsCount; i++){
            System.out.println("\nZadejte jmeno animatora:");
            actors.add(sc.nextLine());
        }
        return actors;
    }

    public static int readInteger(Scanner sc){
        int num;
        while(!sc.hasNextInt()){
            System.out.println("Zadejte cele cislo");
            sc.next();
        }
        num = sc.nextInt();
        sc.nextLine();
        return num;
    }

    public static int readInteger(int minimum, int maximum, Scanner sc){
        int num;
        while(true){
            while(!sc.hasNextInt()){
                System.out.println("Zadejte cele cislo");
                sc.next();
            }
            num = sc.nextInt();
            if(num >= minimum && num <= maximum) break;
            System.out.format("Zadejte cislo v rozsahu %d az %d\n", minimum, maximum);
        }
        sc.nextLine();
        return num;
    }

    public static boolean readYesOrNo(Scanner sc){
        String answer;
        boolean answerBool;
        System.out.println(" (A/N)");
        while(true){
            answer = sc.next();
            if(answer.toLowerCase().equals("a")){
                answerBool = true;
                break;
            }
            else if(answer.toLowerCase().equals("n")) {
                answerBool = false;
                break;
            }
            else System.out.println("Zadejete platnou moznost (A/N)");
        }
        sc.nextLine();
        return answerBool;
    }

    public static void printMovie(Movie movie){
        System.out.println();
        for(int c = 0; c < movie.getName().length(); c++) System.out.print("-");
        System.out.println("\n" + movie.getName());
        for(int c = 0; c < movie.getName().length(); c++) System.out.print("-");
        System.out.println("\nTyp: " + (movie.getMovieType().equals(MovieType.FEATURE) ? "hrany" : "animovany"));
        System.out.println("Reziser: " + movie.getDirectorName());
        System.out.println("Rok vydani: " + movie.getYear());
        if(movie.getMovieType().equals(MovieType.ANIMATED)) {
            System.out.println("Doporuceny vek: " + ((AnimatedMovie)movie).getRecommendedAge());
            System.out.println("\nAnimatori:\n**********");
            for(String animatorName : movie.getSetOfPersonNames()) System.out.println(animatorName);
            System.out.println("\nRecenze:\n********");
            for(Review review : movie.getListOfReviews()){
                System.out.println("Pocet bodu: " + String.valueOf(review.getRating()) + "/10");
                if(review.getReviewText() != null) System.out.println(review.getReviewText());
                System.out.println();
            }
        }
        else {
            System.out.println("\nHerci:\n******");
            for(String actorName : movie.getSetOfPersonNames()) System.out.println(actorName);
            System.out.println("\nRecenze:\n********");
            for(Review review : movie.getListOfReviews()){
                System.out.println("Pocet Hvezd: " + String.valueOf(review.getRating()) + " z 5");
                if(review.getReviewText() != null) System.out.println(review.getReviewText());
                System.out.println();
            }
        }
    }

    public static void printPerson(Person person){
        System.out.println();
        for(int c = 0; c < person.getName().length(); c++) System.out.print("-");
        System.out.println("\n" + person.getName());
        for(int c = 0; c < person.getName().length(); c++) System.out.print("-");
        System.out.print("\nProfese: ");
        for(PersonProfession personProfession : person.getSetOfProfessions()) System.out.print(personProfession.name() + " ");
        System.out.println();
        if(person.getSetOfProfessions().contains(PersonProfession.DIRECTOR)){
            System.out.println("\nReziser:\n********");
            for(String movieName : person.getListOfMovies(PersonProfession.DIRECTOR)) System.out.println(movieName);
        }
        if(person.getSetOfProfessions().contains(PersonProfession.ACTOR)){
            System.out.println("\nHerec:\n******");
            for(String movieName : person.getListOfMovies(PersonProfession.ACTOR)) System.out.println(movieName);
        }
        if(person.getSetOfProfessions().contains(PersonProfession.ANIMATOR)){
            System.out.println("\nAnimator:\n*********");
            for(String movieName : person.getListOfMovies(PersonProfession.ANIMATOR)) System.out.println(movieName);
        }
    }

    public static Review readMovieReview(MovieType movieType, Scanner sc){
        Review review = null;
        int rating;
        String reviewText = null;

        System.out.print("\nChcete zadat slovni hodnoceni filmu?");
        if(readYesOrNo(sc)){
            System.out.println("Zadejte text recenze:");
            reviewText = sc.nextLine();
        }

        switch(movieType){
            case FEATURE:
                System.out.println("\nZadejte ciselne hodnoceni filmu od 1 do 5 hvezd");
                rating = readInteger(1, 5, sc);
                review = new FeatureMovieReview(rating, reviewText);
                break;
            case ANIMATED:
                System.out.println("\nZadejte ciselne hodnoceni filmu od 1 do 10 bodu");
                rating = readInteger(1, 10, sc);
                review = new AnimatedMovieReview(rating, reviewText);
                break;
        }
        return review;
    }
}
