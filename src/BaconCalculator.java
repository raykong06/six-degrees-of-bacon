import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class BaconCalculator {

    private Scanner scanner = new Scanner(System.in);
    private ArrayList<SimpleMovie> movies = MovieDatabaseBuilder.getMovieDB("src/movie_data");
    private ArrayList<SimpleMovie> moviesSortedLargeCast = MovieDatabaseBuilder.getSimpleMovieDB("src/output.txt");
    private ArrayList<String> secondDegreeActors = MovieDatabaseBuilder.getStringFile("src/second_degree_actors.txt");
    private ArrayList<SimpleMovie> secondDegreeMovies = MovieDatabaseBuilder.getSimpleMovieDB("src/second_degree_movies.txt");
    private ArrayList<SimpleMovie> thirdDegreeMovies = MovieDatabaseBuilder.getSimpleMovieDB("src/third_degree_movies.txt");
    private String inputActor;
    private int degree;
    private ArrayList<String> connectedActors;
    private ArrayList<String> connectedMovies;
    private ArrayList<String> allActors;
    private ArrayList<String> kevinBaconCastmates;
    private ArrayList<SimpleMovie> kevinBaconCastmatesMovies;

    public BaconCalculator()
    {
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

    public ArrayList<SimpleMovie> test()
    {
        oneDegreeOfBacon();
        ArrayList<SimpleMovie> mov = new ArrayList<SimpleMovie>();
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
            for (int j = 0; j < moviesWithActor.size(); j++)
            {
                boolean inList = false;
                String currentMovieTitle = moviesWithActor.get(j).getTitle();
                for (int k = 0; k < mov.size(); k++)
                {
                    String compareMovieTitle = mov.get(k).getTitle();
                    if (compareMovieTitle.equals(currentMovieTitle))
                    {
                        inList = true;
                        k = mov.size();
                    }
                }
                if (!inList)
                {
                    mov.add(moviesWithActor.get(j));
                }
            }
        }

        return mov;
    }

    public void setInputActor(String inputActor)
    {
        this.inputActor = inputActor;
    }
    public void calculateBacon()
    {
        oneDegreeOfBacon();
        connectedMovies = new ArrayList<String>();
        connectedActors = new ArrayList<String>();
        ArrayList<String> nextDegreeActorListx = new ArrayList<String>(); // degree 2 actors
        ArrayList<SimpleMovie> nextDegreeMovieListx = new ArrayList<SimpleMovie>(); // degree 2 movies
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
                connectedMovies.add(kevinBaconCastmatesMovies.get(actorIndex).getTitle());
                foundActor = true;
            }
        }
        if (!foundActor) // degree 2
        {
            for (int i = 0; i < secondDegreeMovies.size(); i++)
            {
                SimpleMovie currentMovie = secondDegreeMovies.get(i);
                ArrayList<String> currentMovieCast = currentMovie.getActors();
                if (currentMovieCast.contains(inputActor))
                {
                    for (int j = 0; j < currentMovieCast.size(); j++)
                    {
                        String currentActor = currentMovieCast.get(j);
                        if (kevinBaconCastmates.contains(currentActor))
                        {
                            degree = 2;
                            foundActor = true;

                            String degreeOneActor = currentActor;
                            String degreeOneMovie = "";

                            for (int k = 0; k < kevinBaconCastmatesMovies.size(); k++)
                            {
                                ArrayList<String> cast = kevinBaconCastmatesMovies.get(k).getActors();
                                if (cast.contains(degreeOneActor))
                                {
                                    degreeOneMovie = kevinBaconCastmatesMovies.get(k).getTitle();
                                    k = kevinBaconCastmatesMovies.size();
                                }
                            }

                            connectedMovies.add(currentMovie.getTitle()); // Input actor --> this movie -->
                            connectedActors.add(degreeOneActor); // degree 1 actor -->

                            connectedMovies.add(degreeOneMovie); // degree 1 movie --> Kevin Bacon

                            i = secondDegreeMovies.size();
                            j = currentMovieCast.size();
                        }
                    }
                }
            }

            /*
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

             */
        }


        if (!foundActor) // degree 3
        {
            // check arraylist of third degree movies to see which specific movie both were cast in
            for (int i = 0; i < thirdDegreeMovies.size(); i++) {
                SimpleMovie currentMovie = thirdDegreeMovies.get(i);
                ArrayList<String> currentMovieCast = currentMovie.getActors();
                if (currentMovieCast.contains(inputActor))
                {
                    for (int j = 0; j < currentMovieCast.size(); j++)
                    {
                        String currentActor = currentMovieCast.get(j);
                        if (secondDegreeActors.contains(currentActor)) //&& !currentActor.equals(inputActor))
                        {
                            degree = 3;
                            foundActor = true;

                            //int index = secondDegreeActors.indexOf(currentActor);
                            String degreeTwoActor = currentActor; //secondDegreeActors.get(index);
                            String degreeTwoMovie = "";

                            String degreeOneActor = "";
                            for (int k = 0; k < secondDegreeMovies.size(); k++)
                            {
                                ArrayList<String> cast = secondDegreeMovies.get(k).getActors();
                                boolean hasDegreeTwoActor = false;
                                if (cast.contains(degreeTwoActor))
                                {
                                    hasDegreeTwoActor = true;
                                }
                                boolean hasDegreeOneActor = false;
                                for (int l = 0; l < kevinBaconCastmates.size(); l++)
                                {
                                    degreeOneActor = kevinBaconCastmates.get(l);
                                    if (cast.contains(kevinBaconCastmates.get(l)))
                                    {
                                        hasDegreeOneActor = true;
                                        l = kevinBaconCastmates.size();
                                    }
                                }
                                if (hasDegreeOneActor && hasDegreeTwoActor)
                                {
                                    degreeTwoMovie = secondDegreeMovies.get(k).getTitle();
                                    k = secondDegreeMovies.size();
                                }
                            }

                            connectedMovies.add(currentMovie.getTitle()); // Input actor --> this movie -->
                            connectedActors.add(degreeTwoActor); // degree 2 actor -->

                            connectedMovies.add(degreeTwoMovie); // degree 2 corresponding movie -->
                            connectedActors.add(degreeOneActor); // degree 1 actor -->

                            int degreeOneIndex = runBinarySearch(kevinBaconCastmates, degreeOneActor);
                            connectedMovies.add(kevinBaconCastmatesMovies.get(degreeOneIndex).getTitle()); // degree 1 movie --> Kevin Bacon

                            i = thirdDegreeMovies.size();
                            j = currentMovieCast.size();
                        }
                    }
                }
            }
            /*
            // create an array with all movies that input actor was in
            ArrayList<SimpleMovie> moviesWithInputActor = new ArrayList<SimpleMovie>();
            for (int i = 0; i < moviesSortedLargeCast.size(); i++)
            {
                SimpleMovie currentMovie = moviesSortedLargeCast.get(i);
                ArrayList<String> currentMovieCast = moviesSortedLargeCast.get(i).getActors();
                if (currentMovieCast.contains(inputActor)) {
                    moviesWithInputActor.add(currentMovie);
                }
            }
            // compare all movies input actor was in and compare to third degree movies
            for (int i = 0; i < moviesWithInputActor.size(); i++)
            {
                String currentMovieTitle = moviesWithInputActor.get(i).getTitle();
                for (int j = 0; j < thirdDegreeMovieTitles.size(); j++)
                {
                    String compareMovieTitle = thirdDegreeMovieTitles.get(j);
                    if (compareMovieTitle.equals(currentMovieTitle))
                    {
                        // if same movie was found, continue the method
                        continueSearch = true;
                        i = moviesWithInputActor.size();
                        j = thirdDegreeMovieTitles.size();
                    }
                }
            }

             */
            /*
            for (int i = 0; i < nextDegreeActorList.size(); i++)
            {
                System.out.println(i + "/" + nextDegreeActorList.size());
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



                /*
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

                        int checkIndex = runBinarySearch(nextDegreeActorList, currentActor);
                        if (checkIndex < 0)
                        {
                            nextDegreeActorList.add(currentActor);
                            Collections.sort(nextDegreeActorList);
                            int sortIndex = runBinarySearch(nextDegreeActorList, currentActor);
                            nextDegreeMovieList.add(sortIndex, currentMovie);
                        }


                    }
                }

                 */
/*
            boolean continueSearch = false;
            for (int i = 0; i < moviesSortedLargeCast.size(); i++)
            {
                SimpleMovie currentMovie = moviesSortedLargeCast.get(i);
                ArrayList<String> currentMovieCast = moviesSortedLargeCast.get(i).getActors();
                if (currentMovieCast.contains(inputActor))
                {
                    if (allMoviesCheck.contains(currentMovie)) {
                        continueSearch = true;
                        i = moviesSortedLargeCast.size();
                    }
                }
            }

            if (continueSearch)
            {
                for (int i = 0; i < allMoviesWithActors.size(); i++) {
                    ArrayList<SimpleMovie> currentMovieWithActor = allMoviesWithActors.get(i);
                    for (int j = 0; j < currentMovieWithActor.size(); j++)
                    {
                        SimpleMovie currentMovie = currentMovieWithActor.get(j);
                        ArrayList<String> currentMovieCast = currentMovie.getActors();
                        // found actor
                        if (currentMovieCast.contains(inputActor)) {
                            degree = 3;
                            foundActor = true;

                            String addActor = correspondingActorPrevDegree.get(i);

                            connectedMovies.add(currentMovie.getTitle()); // Input actor --> this movie -->
                            connectedActors.add(nextDegreeActorList.get(i)); // degree 2 actor -->

                            connectedMovies.add(nextDegreeMovieList.get(i).getTitle()); // degree 2 corresponding movie -->
                            connectedActors.add(addActor); // degree 1 actor -->

                            int degreeThreeIndex = runBinarySearch(kevinBaconCastmates, addActor);
                            connectedMovies.add(correspondingKevinBaconCastmates.get(degreeThreeIndex)); // degree 1 movie --> Kevin Bacon

                            j = currentMovieWithActor.size();
                            i = allMoviesWithActors.size();
                        }
                    }
                }
            }

 */
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

                allActors.add(currentCastMember);

            }
        }
    }

    private void oneDegreeOfBacon()
    {
        kevinBaconCastmates = new ArrayList<String>();
        kevinBaconCastmatesMovies = new ArrayList<SimpleMovie>();
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
                        kevinBaconCastmatesMovies.add(addMovieIndex, currentMovie);
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
