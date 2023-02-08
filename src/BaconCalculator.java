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
        System.out.print("hi");
        int i = 1;
        for (String str : kevinBaconCastmates)
        {
            System.out.print("" + i + ". ");
            System.out.println(str);
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
                    int index = -1;

                    while (low <= high)
                    {
                        int mid = low + ((high - low) / 2);
                        if (kevinBaconCastmates.get(mid).compareTo(currentCastMember) < 0)
                        {
                            low = mid + 1;
                        }
                        else if (kevinBaconCastmates.get(mid).compareTo(currentCastMember) > 0)
                        {
                            high = mid - 1;
                        }
                        else if (kevinBaconCastmates.get(mid).compareTo(currentCastMember) == 0)
                        {
                            index = mid;
                            low = high + 1;
                        }
                    }

                    if (index == -1)
                    {
                        inList = false;
                    }
                    else
                    {
                        inList = true;
                    }
                /*
                int low = 0;
                int high = kevinBaconCastmates.size() - 1;
                int target = low + ((high - low) / 2);

                if (kevinBaconCastmates.size() != 0 && kevinBaconCastmates.get(target).equals(currentCastMember))
                {
                    inList = true;
                }
                if (kevinBaconCastmates.size() != 0)
                {
                    while (!kevinBaconCastmates.get(target).equals(currentCastMember) || high != low)
                    {
                        int compare = currentCastMember.compareTo(kevinBaconCastmates.get(target));
                        if (compare < 0)
                        {
                            high = target - 1;
                        }
                        if (compare > 0)
                        {
                            low = target + 1;
                        }
                        target = low + ((high - low) / 2);
                    }
                }

                 */
                /*
                for (int k = 0; k < kevinBaconCastmates.size(); k++)
                {
                    if (currentCastMember.equals(kevinBaconCastmates.get(k)))
                    {
                        inList = true;
                        k = kevinBaconCastmates.size();
                    }
                }

                 */
                    if (!inList)
                    {
                        kevinBaconCastmates.add(currentCastMember);
                        //sortStringResults(kevinBaconCastmates);
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
}
