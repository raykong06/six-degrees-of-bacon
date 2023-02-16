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

                if (matches.size() < 1)
                {
                    System.out.println("Your search for an actor had no matches within the database.");
                }
                else
                {
                    System.out.println("\nWhich actor do you want to pick?");
                    System.out.print("Enter a number: ");
                    int choice = scanner.nextInt();

                    inputActor = matches.get(choice - 1);
                    System.out.println("\nActor chosen: " + matches.get(choice - 1));

                    calculateBacon();

                    if (degree < 0)
                    {
                        System.out.println("The actor could not be found in 3 degrees or less.");
                    }
                    else
                    {
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
                    }
                    scanner.nextLine();
                }

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
        ArrayList<String> nextDegreeActorList = new ArrayList<String>();
        ArrayList<SimpleMovie> nextDegreeMovieList = new ArrayList<SimpleMovie>();
        ArrayList<String> correspondingActorPrevDegree = new ArrayList<String>();
        boolean foundActor = false;

        if (inputActor.equals("Kevin Bacon")) // degree 0
        {
            degree = 0;
            foundActor = true;
        }
        if (!foundActor) // degree 1
        {
            int actorIndex = runBinarySearch(kevinBaconCastmates, inputActor);
            if (actorIndex > -1)
            {
                degree = 1;
                connectedMovies.add(correspondingKevinBaconCastmates.get(actorIndex));
                foundActor = true;
            }
        }
        if (!foundActor) // degree 2
        {
            for (int i = 0; i < kevinBaconCastmates.size(); i++) {
                String currentActor = kevinBaconCastmates.get(i);
                ArrayList<SimpleMovie> moviesWithActor = new ArrayList<SimpleMovie>();
                for (int j = 0; j < moviesSortedLargeCast.size(); j++) {
                    SimpleMovie currentMovie = moviesSortedLargeCast.get(j);
                    ArrayList<String> currentMovieCast = moviesSortedLargeCast.get(j).getActors();
                    if (currentMovieCast.contains(currentActor)) {
                        moviesWithActor.add(currentMovie);
                    }
                }
                for (int j = 0; j < moviesWithActor.size(); j++) {
                    SimpleMovie currentMovie = moviesWithActor.get(j);
                    ArrayList<String> currentMovieCast = currentMovie.getActors();
                    for (int k = 0; k < currentMovieCast.size(); k++)
                    {
                        String currentMovieCurrentActor = currentMovieCast.get(k);

                        // found actor
                        if (currentMovieCurrentActor.equals(inputActor)) {
                            degree = 2;
                            foundActor = true;
                            j = moviesWithActor.size();
                            i = kevinBaconCastmates.size();

                            connectedMovies.add(currentMovie.getTitle()); // Input actor --> this movie -->
                            connectedActors.add(currentActor);  // kevinBaconCastmate -->

                            int degreeTwoIndex = runBinarySearch(kevinBaconCastmates, currentActor);
                            connectedMovies.add(correspondingKevinBaconCastmates.get(degreeTwoIndex)); // kevinBaconCastmateCorrespond --> Kevin Bacon
                        }
                        else
                        {
                            int checkIndex = runBinarySearch(nextDegreeActorList, currentMovieCurrentActor);
                            if (checkIndex < 0)
                            {
                                nextDegreeActorList.add(currentMovieCurrentActor);
                                Collections.sort(nextDegreeActorList);
                                int sortIndex = runBinarySearch(nextDegreeActorList, currentMovieCurrentActor);
                                nextDegreeMovieList.add(sortIndex, currentMovie);
                                correspondingActorPrevDegree.add(sortIndex, currentActor);
                            }
                        }
                    }
                }
            }
        }


        if (!foundActor) // degree 3
        {
            for (int i = 0; i < nextDegreeActorList.size(); i++)
            {
                String currentActor = nextDegreeActorList.get(i);
                SimpleMovie correspondingMovie = nextDegreeMovieList.get(i);
                ArrayList<SimpleMovie> moviesWithActor = new ArrayList<SimpleMovie>();

                for (int j = 0; j < moviesSortedLargeCast.size(); j++) {
                    SimpleMovie currentMovie = moviesSortedLargeCast.get(j);
                    ArrayList<String> currentMovieCast = moviesSortedLargeCast.get(j).getActors();
                    if (currentMovieCast.contains(currentActor)) {
                        moviesWithActor.add(currentMovie);
                    }
                }


                for (int j = 0; j < moviesWithActor.size(); j++) {
                    SimpleMovie currentMovie = moviesWithActor.get(j);
                    ArrayList<String> currentMovieCast = currentMovie.getActors();
                    // found actor
                    if (currentMovieCast.contains(inputActor)) {
                        degree = 3;
                        foundActor = true;

                        String addActor = correspondingActorPrevDegree.get(i);

                        connectedMovies.add(currentMovie.getTitle()); // Input actor --> this movie -->
                        connectedActors.add(currentActor); // degree 2 actor -->

                        connectedMovies.add(correspondingMovie.getTitle()); // degree 2 corresponding movie -->
                        connectedActors.add(addActor); // degree 1 actor -->

                        int degreeThreeIndex = runBinarySearch(kevinBaconCastmates, addActor);
                        connectedMovies.add(correspondingKevinBaconCastmates.get(degreeThreeIndex)); // degree 1 movie --> Kevin Bacon

                        j = moviesWithActor.size();
                        i = nextDegreeActorList.size();
                    }
                    else
                    {
                        /*
                        int checkIndex = runBinarySearch(nextDegreeActorList, currentActor);
                        if (checkIndex < 0)
                        {
                            nextDegreeActorList.add(currentActor);
                            Collections.sort(nextDegreeActorList);
                            int sortIndex = runBinarySearch(nextDegreeActorList, currentActor);
                            nextDegreeMovieList.add(sortIndex, currentMovie);
                        }

                         */
                    }
                }
            }
        }

        if (!foundActor)
        {
            degree = -1;
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

                int index = runBinarySearch(allActors, currentCastMember);

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

                    int index = runBinarySearch(kevinBaconCastmates, currentCastMember);

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

                        int addMovieIndex = runBinarySearch(kevinBaconCastmates, currentCastMember);
                        correspondingKevinBaconCastmates.add(addMovieIndex, currentMovie.getTitle());
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

    private int runBinarySearch(ArrayList<String> sortedArray, String compare)
    {
        int low = 0;
        int high = sortedArray.size() - 1;

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
