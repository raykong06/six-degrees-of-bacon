import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayList;

public class BaconCalculator {

    ArrayList<SimpleMovie> movies = MovieDatabaseBuilder.getMovieDB("src/movie_data");
    private String inputActor;
    private int degree;
    private ArrayList<String> kevinBaconCastmates;

    public BaconCalculator(String inputActor)
    {
        this.inputActor = inputActor;
        oneDegreeOfBacon();
    }

    public void printKevinBaconCastmates()
    {
        int i = 1;
        for (String str : kevinBaconCastmates)
        {
            System.out.print("" + i + ". ");
            System.out.println(str);
            i++;
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
        for (int i = 1; i < listToSort.size(); i++)
        {
            String temp = listToSort.get(i);
            int possibleIndex = i;
            while (possibleIndex > 0 && temp.compareTo(listToSort.get(possibleIndex - 1)) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
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
