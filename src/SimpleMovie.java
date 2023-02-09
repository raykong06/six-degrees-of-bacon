import java.util.ArrayList;

public class SimpleMovie implements Comparable<SimpleMovie>{
    private String title;
    private String actorsData;
    private ArrayList<String> actors;

    public SimpleMovie(String t, String a) {
        title = t;
        actorsData = a;
        actors = new ArrayList<String>();
        String[] tempActors;
        if (actorsData.indexOf(":") > -1)
        {
            tempActors = actorsData.split(":");
        }
        else
        {
            tempActors = actorsData.split(", ");
        }

        for (int i = 0; i < tempActors.length; i++) {
            actors.add(tempActors[i]);
        }

    }

    public ArrayList<String> getActors()
    {
        return actors;
    }
    public String getTitle()
    {
        return title;
    }

    public String toString() {
        return "Title: " + title + "\n" + "Actors: " + actors + "\n";
    }

    @Override
    public int compareTo(SimpleMovie movie) {
        return actors.size() - movie.getActors().size();
    }
}