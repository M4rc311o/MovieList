package movie_list;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBOperations {
    public void insertMovie(Movie movie){
        if(movie == null) throw new NullPointerException();

        Connection conn = DBConnection.getDBConnection();
        String insertMovie = "INSERT INTO movies(movie_type_id,name,year,director_id,recommended_age) VALUES(?,?,?,?,?)";
        int directorId = getPersonId(movie.getDirectorName());

        try(PreparedStatement prStmt = conn.prepareStatement(insertMovie);){
            prStmt.setInt(1, movie.getMovieType().equals(MovieType.FEATURE) ? 0 : 1);
            prStmt.setString(2, movie.getName());
            prStmt.setInt(3, movie.getYear());
            prStmt.setInt(4, directorId);
            if(movie.getMovieType().equals(MovieType.FEATURE)) prStmt.setNull(5, Types.INTEGER);
            else prStmt.setInt(5, ((AnimatedMovie)movie).getRecommendedAge());
            prStmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void insertPerson(String personName){
        if(personName == null) throw new NullPointerException();

        Connection conn = DBConnection.getDBConnection();
        String insertPerson = "INSERT INTO people(name) VALUES(?);";

        try(PreparedStatement prStmt = conn.prepareStatement(insertPerson);){
            prStmt.setString(1, personName);
            prStmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void insertMovieReviews(String movieName, List<Review>listOfReviews){
        if(movieName == null || listOfReviews == null) throw new NullPointerException();

        Connection conn = DBConnection.getDBConnection();
        String insertMovieReview = "INSERT INTO reviews(movie_id,rating,review_text) VALUES(?,?,?);";
        int movieId = getMovieId(movieName);

        try(PreparedStatement prStmt = conn.prepareStatement(insertMovieReview);){
            for(Review review : listOfReviews){
                prStmt.setInt(1, movieId);
                prStmt.setInt(2, review.getRating());
                prStmt.setString(3, review.getReviewText());
                prStmt.executeUpdate();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void linkPeopleToMovie(String movieName, Set<String>peopleList){
        if(movieName == null || peopleList == null) throw new NullPointerException();

        Connection conn = DBConnection.getDBConnection();
        String linkPersonToMovie = "INSERT INTO cast(movie_id,person_id) VALUES(?,?);";
        int movieId = getMovieId(movieName);
        int personId;

        try(PreparedStatement prStmt = conn.prepareStatement(linkPersonToMovie);){
            for(String personName : peopleList){
                personId = getPersonId(personName);
                prStmt.setInt(1, movieId);
                prStmt.setInt(2, personId);
                prStmt.executeUpdate();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private int getPersonId(String personName){
        if(personName == null) throw new NullPointerException();
        Connection conn = DBConnection.getDBConnection();

        String selectPersonId = "SELECT id FROM people WHERE name = ?;";
        int id = 0;

        try(PreparedStatement prStmt = conn.prepareStatement(selectPersonId);){
            prStmt.setString(1, personName);
            ResultSet rs = prStmt.executeQuery();
            if(rs.next()) id = rs.getInt("id");  
        } catch(SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    private int getMovieId(String movieName){
        if(movieName == null) throw new NullPointerException();
        Connection conn = DBConnection.getDBConnection();

        String selectMovieId = "SELECT id FROM movies WHERE name = ?;";
        int id = 0;

        try(PreparedStatement prStmt = conn.prepareStatement(selectMovieId);){
            prStmt.setString(1, movieName);
            ResultSet rs = prStmt.executeQuery();
            if(rs.next()) id = rs.getInt("id"); 
        } catch(SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public List<Movie> selectAndConstructAllMovies(){
        Connection conn = DBConnection.getDBConnection();
        List<Movie> listOfMovies = new ArrayList<Movie>();

        String selectAllMovies = "SELECT movies.id AS movies_id, movies.movie_type_id AS movies_movie_type_id, movies.name AS movies_name, movies.year AS movies_year, movies.recommended_age AS movies_recommended_age, people.name AS people_name FROM movies JOIN people ON movies.director_id = people.id;";
        String selectAllPeopleForMovie = "SELECT people.name AS people_name FROM cast JOIN people ON people.id = person_id WHERE movie_id = ?;";
        String selectAllReviewsForMovie = "SELECT rating, review_text FROM reviews WHERE movie_id = ?";

        try(Statement stmt = conn.createStatement();){
            ResultSet rsMovies = stmt.executeQuery(selectAllMovies);
            PreparedStatement prStmtPeople = conn.prepareStatement(selectAllPeopleForMovie);
            PreparedStatement prStmtReviews = conn.prepareStatement(selectAllReviewsForMovie);
            while(rsMovies.next()){

                prStmtPeople.setInt(1, rsMovies.getInt("movies_id"));
                ResultSet rsPeople = prStmtPeople.executeQuery();
                Set<String> setOfPeopleNames = new HashSet<String>();
                while(rsPeople.next()){
                    String personName = rsPeople.getString("people_name");
                    setOfPeopleNames.add(personName);
                }

                prStmtReviews.setInt(1, rsMovies.getInt("movies_id"));
                ResultSet rsReviews = prStmtReviews.executeQuery();
                List<Review> reviews = new ArrayList<Review>();
                while(rsReviews.next()){
                    if(rsMovies.getInt("movies_movie_type_id") == 0) reviews.add(new FeatureMovieReview(rsReviews.getInt("rating"), rsReviews.getString("review_text")));
                    else reviews.add(new AnimatedMovieReview(rsReviews.getInt("rating"), rsReviews.getString("review_text")));
                }

                if(rsMovies.getInt("movies_movie_type_id") == 0){
                    listOfMovies.add(new FeatureMovie(rsMovies.getString("movies_name"), rsMovies.getString("people_name"), rsMovies.getInt("movies_year"), setOfPeopleNames, reviews));
                }
                else{
                    listOfMovies.add(new AnimatedMovie(rsMovies.getString("movies_name"), rsMovies.getString("people_name"), rsMovies.getInt("movies_year"), rsMovies.getInt("movies_recommended_age"), setOfPeopleNames, reviews));
                }
            }
            prStmtPeople.close();
            prStmtReviews.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return listOfMovies;
    }

    public void deleteAllRecords(){
        Connection conn = DBConnection.getDBConnection();
        String dropPeopleTable = "DROP TABLE IF EXISTS people;";
        String dropMoviesTable = "DROP TABLE IF EXISTS movies;";
        String dropCastTable = "DROP TABLE IF EXISTS cast;";
        String dropMovieTypeTable = "DROP TABLE IF EXISTS movie_type;";
        String dropReviewsTable = "DROP TABLE IF EXISTS reviews;";

        try(Statement stmt = conn.createStatement()){
            stmt.executeUpdate(dropPeopleTable);
            stmt.executeUpdate(dropMoviesTable);
            stmt.executeUpdate(dropCastTable);
            stmt.executeUpdate(dropMovieTypeTable);
            stmt.executeUpdate(dropReviewsTable);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void initialiseNewDatabase(){
        String createMoviesTable = "CREATE TABLE IF NOT EXISTS movies (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "movie_type_id INT REFERENCES movie_type(id)," +
        "name VARCHAR(255) NOT NULL," +
        "year INT NOT NULL," +
        "director_id INT REFERENCES people(id)," +
        "recommended_age INT," +
        "UNIQUE (name));";

        String createMovieTypeTable = "CREATE TABLE IF NOT EXISTS movie_type (" +
        "id INTEGER PRIMARY KEY," +
        "type VARCHAR(10) NOT NULL);";

        String createPeopleTable = "CREATE TABLE IF NOT EXISTS people (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "name VARCHAR(255) NOT NULL," +
        "UNIQUE (name));";

        String createCastTable = "CREATE TABLE IF NOT EXISTS cast (" +
        "movie_id INT REFERENCES movies(id)," +
        "person_id INT REFERENCES people(id));";

        String createReviewsTable = "CREATE TABLE IF NOT EXISTS reviews (" +
        "movie_id INT REFERENCES movie(id)," +
        "rating INT NOT NULL," +
        "review_text TEXT);";

        String insertMovieTypes = "INSERT INTO movie_type(id,type) VALUES(0,'" + MovieType.FEATURE.toString() + "'),(1,'" + MovieType.ANIMATED.toString() + "');";

        Connection conn = DBConnection.getDBConnection();
        try(Statement stmt = conn.createStatement();){
            stmt.executeUpdate(createMoviesTable);
            stmt.executeUpdate(createMovieTypeTable);
            stmt.executeUpdate(createPeopleTable);
            stmt.executeUpdate(createCastTable);
            stmt.executeUpdate(createReviewsTable);
            stmt.executeUpdate(insertMovieTypes);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
