import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class BaconCalculator {

    private Scanner scanner = new Scanner(System.in);
    private ArrayList<SimpleMovie> movies = MovieDatabaseBuilder.getMovieDB("src/movie_data");
    private ArrayList<SimpleMovie> moviesSortedLargeCast = MovieDatabaseBuilder.getSimpleMovieDB("src/output.txt");
    private String inputActor;
    private int degree;
    private ArrayList<String> connectedActors;
    private ArrayList<String> connectedMovies;
    private ArrayList<String> allActors;
    private ArrayList<String> kevinBaconCastmates;
    private ArrayList<String> correspondingKevinBaconCastmates;

    public BaconCalculator()
    {
        oneDegreeOfBacon();
        setAllActors();
        //sortMoviesByCast();
    }

    public void printListWithNumbers(ArrayList<String> list)
    {
        int i = 1;
        for (String str : list)
        {
            System.out.print("" + i + ". ");
            System.out.println(str);
            i++;
        }
    }

    public void mainMenu()
    {
        String mainChoice = "";
        while (!mainChoice.equals("q"))
        {
            System.out.print("--------- Bacon Calculator ---------" +
                    "\nEnter an actor's name or (q) to quit: ");
            mainChoice = scanner.nextLine();
            if (!mainChoice.equals("q"))
            {
                inputActor = mainChoice;
                ArrayList<String> matches = new ArrayList<String>();
                for (int i = 0; i < allActors.size(); i++)
                {
                    String addActor = allActors.get(i);
                    boolean inList = false;
                    for (int j = 0; j < matches.size(); j++)
                    {
                        if (matches.get(j).equals(addActor))
                        {
                            inList = true;
                            j = matches.size();
                        }
                    }
                    if (addActor.indexOf(inputActor) > -1 && !inList)
                    {
                        matches.add(addActor);
                    }
                }

                sortStringResults(matches);
                printListWithNumbers(matches);

                System.out.println("\nWhich actor do you want to pick?");
                System.out.print("Enter a number: ");
                int choice = scanner.nextInt();

                inputActor = matches.get(choice - 1);
                System.out.println("\nActor chosen: " + matches.get(choice - 1));

                calculateBacon();

                System.out.print(inputActor + " -> ");
                for (int i = 0; i < connectedMovies.size(); i++)
                {
                    System.out.print(connectedMovies.get(i));
                    System.out.print(" -> ");
                    if (i < connectedActors.size())
                    {
                        System.out.print(connectedActors.get(i));
                        System.out.print(" -> ");
                    }
                }
                System.out.println("Kevin Bacon");
                System.out.println("Bacon Number of: " + degree);

                scanner.nextLine();
                System.out.println();
            }
        }
        System.out.println("\n" +
                "-----------------------------------------" +
                "\nThank you for using the Bacon Calculator!");
    }

    public void test()
    {
        for (int i = 0; i < movies.size(); i++)
        {
            System.out.println(moviesSortedLargeCast.get(i).getActors().size());
        }
    }

    private void calculateBacon()
    {
        connectedMovies = new ArrayList<String>();
        connectedActors = new ArrayList<String>();
        if (inputActor.equals("Kevin Bacon"))
        {
            degree = 0;
        }
        else
        {
            int actorIndex = runBinarySearch(kevinBaconCastmates, inputActor, 0, kevinBaconCastmates.size() - 1);
            if (actorIndex > -1)
            {
                degree = 1;
                connectedMovies.add(0, correspondingKevinBaconCastmates.get(actorIndex));
            }
            else
            {
                degree = 2;

            }
        }
    }

    private void setAllActors()
    {
        allActors = new ArrayList<String>();
        for (int i = 0; i < movies.size(); i++)
        {
            SimpleMovie currentMovie = movies.get(i);
            ArrayList<String> currentMovieCast = currentMovie.getActors();

            for (int j = 0; j < currentMovieCast.size(); j++) {
                String currentCastMember = currentMovieCast.get(j);
                boolean inList = false;

                int low = 0;
                int high = allActors.size() - 1;

                int index = runBinarySearch(allActors, currentCastMember, low, high);

                if (index == -1) {
                    inList = false;
                } else {
                    inList = true;
                }
                if (!inList) {
                    allActors.add(currentCastMember);
                }
            }
        }
    }

    private void oneDegreeOfBacon()
    {
        kevinBaconCastmates = new ArrayList<String>();
        correspondingKevinBaconCastmates = new ArrayList<String>();
        for (int i = 0; i < movies.size(); i++)
        {
            SimpleMovie currentMovie = movies.get(i);
            ArrayList<String> currentMovieCast = currentMovie.getActors();
            boolean bacon = false;
            for (int k = 0; k < currentMovieCast.size(); k++)
            {
                if (currentMovieCast.get(k).equals("Kevin Bacon"))
                {
                    bacon = true;
                    k = currentMovieCast.size();
                }
            }
            if (bacon)
            {
                for (int j = 0; j < currentMovieCast.size(); j++)
                {
                    String currentCastMember = currentMovieCast.get(j);
                    boolean inList = false;

                    int low = 0;
                    int high = kevinBaconCastmates.size() - 1;

                    int index = runBinarySearch(kevinBaconCastmates, currentCastMember, low, high);

                    if (index == -1 && !currentCastMember.equals("Kevin Bacon"))
                    {
                        inList = false;
                    }
                    else
                    {
                        inList = true;
                    }
                    if (!inList)
                    {
                        kevinBaconCastmates.add(currentCastMember);
                        sortStringResults(kevinBaconCastmates);

                        int addMovieIndex = runBinarySearch(kevinBaconCastmates, currentCastMember, 0, kevinBaconCastmates.size());
                        correspondingKevinBaconCastmates.add(addMovieIndex, currentMovie.getTitle());
                    }
                }
            }
        }
    }

    private void extraDegreeOfBacon(ArrayList<String> listToSearch)
    {
        ArrayList<String> finalActorConnect = new ArrayList<String>();
        ArrayList<String> finalMovieConnect = new ArrayList<String>();

        boolean foundKevinBacon = false;
        /*
        while (!foundKevinBacon)
        {
            SimpleMovie currentMovie = moviesSortedLargeCast.get(i);
            for (int j = 0; j < currentMovie.getActors().size(); j++)
            {
                String currentActor = currentMovie.getActors().get(j);
                if (currentActor.equals(inputActor))
                {
                    actorConnect.add(actorConnect)
                }
            }
        }

         */

        for (int i = 0; i < moviesSortedLargeCast.size(); i++)
        {
            ArrayList<String> actorConnect = new ArrayList<String>();
            ArrayList<String> movieConnect = new ArrayList<String>();
            SimpleMovie currentMovie = moviesSortedLargeCast.get(i);
            boolean foundMovie = false;
            for (int j = 0; j < currentMovie.getActors().size(); j++)
            {
                String currentActor = currentMovie.getActors().get(j);
                if (currentActor.equals(inputActor))
                {

                    j = currentMovie.getActors().size();
                    foundMovie = true;
                }
            }
            movieConnect.add(currentMovie.getTitle());
            if (foundMovie)
            {
                for (int j = 0; j < currentMovie.getActors().size(); j++)
                {
                    for (int k = 0; k < moviesSortedLargeCast.size(); k++)
                    {

                    }
                }
            }
        }
    }

    private void findConnectingActor(SimpleMovie movie, ArrayList<String> movieConnect, ArrayList<String> actorConnect)
    {
        boolean foundBacon = false;
        int currentDegree = 0;

        while (!foundBacon && currentDegree <= 5)
        {
            for (int i = 0; i < movie.getActors().size(); i++)
            {
                String currentActor = movie.getActors().get(i);
                actorConnect.add(currentActor);
                for (int j = 0; j < moviesSortedLargeCast.size(); j++)
                {
                    SimpleMovie currentMovie = moviesSortedLargeCast.get(j);
                    movieConnect.add(currentMovie.getTitle());
                    for (int k = 0; k < currentMovie.getActors().size(); k++)
                    {
                        String actor = currentMovie.getActors().get(k);
                        if (actor.equals("Kevin Bacon"))
                        {
                            foundBacon = true;
                            k = currentMovie.getActors().size();
                            j = moviesSortedLargeCast.size();
                        }
                    }
                }
            }
        }
    }

    /*
    private void sortMoviesByCast()
    {
        moviesSortedLargeCast = new ArrayList<SimpleMovie>();

        for (int i = 0; i < movies.size(); i++)
        {
            SimpleMovie currentMovie = movies.get(i);
            moviesSortedLargeCast.add(currentMovie);
            Collections.sort(moviesSortedLargeCast, Collections.reverseOrder());
        }
    }

    public ArrayList<SimpleMovie> makeFile()
    {
        moviesSortedLargeCast = new ArrayList<SimpleMovie>();

        for (int i = 0; i < movies.size(); i++)
        {
            SimpleMovie currentMovie = movies.get(i);
            moviesSortedLargeCast.add(currentMovie);
            Collections.sort(moviesSortedLargeCast, Collections.reverseOrder());
        }

        return moviesSortedLargeCast;
    }

     */

    private void sortStringResults(ArrayList<String> listToSort)
    {
        Collections.sort(listToSort);
    }

    private int runBinarySearch(ArrayList<String> sortedArray, String compare, int low, int high)
    {
        int index = -1;

        while (low <= high)
        {
            int mid = low + ((high - low) / 2);
            if (sortedArray.get(mid).compareTo(compare) < 0)
            {
                low = mid + 1;
            }
            else if (sortedArray.get(mid).compareTo(compare) > 0)
            {
                high = mid - 1;
            }
            else if (sortedArray.get(mid).compareTo(compare) == 0)
            {
                index = mid;
                low = high + 1;
            }
        }
        return index;
    }
}
