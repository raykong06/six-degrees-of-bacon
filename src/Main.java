import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        //ArrayList<SimpleMovie> movies = MovieDatabaseBuilder.getMovieDB("src/movie_data");
        //for (SimpleMovie movie : movies) {
        //    System.out.println(movie);
        //}
        //System.out.println("Number of movies: " + movies.size());

        BaconCalculator bc = new BaconCalculator();
        bc.mainMenu();
    }
}

/*
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {

        BaconCalculator bc = new BaconCalculator();
        bc.setInputActor("Andrew Zimmern");
        ArrayList<SimpleMovie> names = bc.test();
        try {
            File f = new File("src/second_degree_movies.txt");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            for (SimpleMovie n : names)
            {
                fw.write(n + "\n");
            }

            fw.close();
        }
        catch (IOException ioe) {
            System.out.println("Writing file failed");
            System.out.println(ioe);
        }


    }
}

 */
