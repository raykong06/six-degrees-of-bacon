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
            for (int j = 0; j < currentMovieCast.size(); j++)
            {
                String currentCastMember = currentMovieCast.get(j);
                boolean inList = false;
                for (int k = 0; k < kevinBaconCastmates.size(); k++)
                {
                    if (currentCastMember.equals(kevinBaconCastmates.get(k)))
                    {
                        inList = true;
                        k = kevinBaconCastmates.size();
                    }
                }
                if (!inList)
                {
                    kevinBaconCastmates.add(currentCastMember);
                }
            }
        }
    }
}
