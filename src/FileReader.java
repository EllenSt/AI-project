import java.io.*;
import java.util.*;

public class FileReader {
    public static void readLessons(ArrayList[] indexer) {
        Scanner lessonsReader;
        try {

            lessonsReader = new Scanner(new File("lessons.txt"));
            while(lessonsReader.hasNextLine()) {
                String id = lessonsReader.nextLine();
                String name = lessonsReader.nextLine();
                int classTaught = Integer.valueOf(lessonsReader.nextLine().trim());
                int hours = Integer.valueOf(lessonsReader.nextLine().trim());
                indexer[classTaught-1].add(new Lesson(id,name, classTaught, hours));
            }

            lessonsReader.close();

        } catch (Exception e) {
            System.out.println("Error with lessons file: " + e.getMessage());
        }
    }

    public static void readTeachers(ArrayList<Teacher> teachers) {
        Scanner teachersReader;
        try {

            teachersReader = new Scanner(new File("teachers.txt"));
            while(teachersReader.hasNextLine()) {
                String id = teachersReader.nextLine();
                String name = teachersReader.nextLine();
                ArrayList<String> lessons = new ArrayList();
                String lessonId = teachersReader.nextLine();
                while(!lessonId.equals("-")){
                    lessons.add(lessonId.trim());
                    lessonId = teachersReader.nextLine();
                }
                ArrayList<Integer> daily_hours = new ArrayList();
                String hours = teachersReader.nextLine();
                while(!hours.equals("-")){
                    daily_hours.add(Integer.valueOf(hours.trim()));
                    hours = teachersReader.nextLine();
                }
                teachers.add(new Teacher(id,name,lessons,daily_hours));
            }

            teachersReader.close();

        } catch (Exception e) {
            System.out.println("Error with teachers file: "  + e.getMessage());
        }
    }

}
