import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;

public class scheduleWriter {

    public static void writeToTextFIle(Chromosome sched){

        BufferedWriter bw;
        FileWriter fw;

        try {

            fw = new FileWriter("schedule.txt");
            bw = new BufferedWriter(fw);

            for(int i=0;i<sched.getGenes().size();i++){
                bw.write((sched.getGeneAt(i)).getLesson().getLessonName() + " by " + sched.getGeneAt(i).getTeacher().getTeacherName() + " ") ;

                //System.out.print((genes.elementAt(i)).getLesson().getLessonID() + " ");
                if((i+1)%(Globals.DAYS*Globals.CLASSES)==0 && i!=0){
                    bw.write("\n");
                }
            }

            bw.close();
            fw.close();
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static String getFullLine(TimeSlot slot){
        return slot.getLesson().getLessonID() + String.join("", Collections.nCopies(28 - slot.getLesson().getLessonID().length() ," ")) + " by "
        + slot.getTeacher().getTeacherID() + String.join("", Collections.nCopies(28 - slot.getTeacher().getTeacherID().length() ," "));
    }


    public static String getCLass(int index){
        switch (index){
            case 0:
                return "A1";
            case 1:
                return "A2";
            case 2:
              return "A3";
            case 3:
              return "B1";
            case 4:
              return "B2";
            case 5:
              return "B3";
            case 6:
              return "C1";
            case 7:
               return "C2";
            case 8:
               return "C3";
        }
        return "";
    }
}
