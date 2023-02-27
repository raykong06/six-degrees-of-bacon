import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;

public class MovieDatabaseBuilder {
    public static ArrayList<SimpleMovie> getMovieDB(String fileName) {
        ArrayList<SimpleMovie> movies = new ArrayList<SimpleMovie>();
        try {
            File movieData = new File(fileName);
            Scanner reader = new Scanner(movieData);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split("---");
                if (data.length > 1) {
                    SimpleMovie s = new SimpleMovie(data[0], data[1]);
                    movies.add(s);
                }

            }
        }
        catch (FileNotFoundException noFile) {
            System.out.println("File not found!");
            return null;
        }
        return movies;
    }

    public static ArrayList<SimpleMovie> getSimpleMovieDB(String fileName) {
        ArrayList<SimpleMovie> movies = new ArrayList<SimpleMovie>();
        try {
            File movieData = new File(fileName);
            Scanner reader = new Scanner(movieData);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.indexOf("Title: ") > -1)
                {
                    String title = line.substring(line.indexOf("Title: ") + 7);
                    String nextLine = reader.nextLine();
                    String actors = nextLine.substring(nextLine.indexOf("Actors: [") + 9, nextLine.length() - 1);
                    String[] data = {title, actors};
                    if (data.length > 1) {
                        SimpleMovie s = new SimpleMovie(data[0], data[1]);
                        movies.add(s);
                    }
                }

            }
        }
        catch (FileNotFoundException noFile) {
            System.out.println("File not found!");
            return null;
        }
        return movies;
    }

    public static ArrayList<ArrayList<SimpleMovie>> getSimpleMovieArrayDB(String fileName) {
        ArrayList<ArrayList<SimpleMovie>> arrMovies = new ArrayList<ArrayList<SimpleMovie>>();
        try {
            File movieData = new File(fileName);
            Scanner reader = new Scanner(movieData);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.indexOf("[Title:") == 0)
                {
                    ArrayList<SimpleMovie> movies = new ArrayList<SimpleMovie>();
                    String closeBracket = "";
                    while (!closeBracket.equals("]"))
                    {
                        String title = line.substring(line.indexOf("Title: ") + 7);
                        String nextLine = reader.nextLine();
                        //System.out.println(nextLine);
                        String actors = nextLine.substring(nextLine.indexOf("Actors: [") + 9, nextLine.length() - 1);
                        String[] data = {title, actors};
                        if (data.length > 1) {
                            SimpleMovie s = new SimpleMovie(data[0], data[1]);
                            movies.add(s);
                        }
                        line = reader.nextLine();
                        if (line.equals("]"))
                        {
                            closeBracket = line;
                        }
                    }
                    arrMovies.add(movies);
                }
            }
        }
        catch (FileNotFoundException noFile) {
            System.out.println("File not found!");
            return null;
        }
        return arrMovies;
    }

    public static ArrayList<String> getStringFile(String fileName) {
        ArrayList<String> movies = new ArrayList<String>();
        try {
            File movieData = new File(fileName);
            Scanner reader = new Scanner(movieData);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.length() > 1) {
                    movies.add(line);
                }
            }
        }
        catch (FileNotFoundException noFile) {
            System.out.println("File not found!");
            return null;
        }
        return movies;
    }
}