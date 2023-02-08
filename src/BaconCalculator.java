import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class BaconCalculator {

    Scanner scanner = new Scanner(System.in);
    ArrayList<SimpleMovie> movies = MovieDatabaseBuilder.getMovieDB("src/movie_data");
    private String inputActor;
    private int degree;
    private ArrayList<String> allActors;
    private ArrayList<String> kevinBaconCastmates;

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

    public void calculateBacon()
    {
        System.out.print("Enter an actor's name or (q) to quit: ");
        inputActor = scanner.nextLine();
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
                    }
                }
            }
        }
    }

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

    private ArrayList<Integer> runBinarySearchContains(ArrayList<String> sortedArray, String compare, int low, int high)
    {
        ArrayList<Integer> index = new ArrayList<Integer>();

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
                index.add(mid);
                low = high + 1;
            }
        }
        return index;
    }
}
