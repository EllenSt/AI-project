import java.util.ArrayList;

public class Teacher {

    private String teacherID;
	private String teacherName;
	private int maxPerWeek;
    private ArrayList<String> lessonsTaught;
    private ArrayList<Integer> maxPerDay = new ArrayList<>();
    private ArrayList<Integer> teachingPointsPerDay;
    private int teachingPoints;
    private double teachingPointsAverage;

    public Teacher(String teacherID, String teacherName, ArrayList<String> lessonsTaught, ArrayList<Integer> maxPerDay) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.lessonsTaught =  lessonsTaught;
        this.maxPerDay =  maxPerDay;
        teachingPointsPerDay = new ArrayList<>();
        maxPerWeek=0;
        teachingPoints = 0;
        teachingPointsAverage = 0;
        for(int k=0;k<Globals.DAYS;k++){
            teachingPointsPerDay.add(0);
            maxPerWeek +=this.maxPerDay.get(k);
        }
    }

    public Teacher() {
        this.teacherID = Globals.EMPTY_TEACHER_ID;
        this.teacherName = "No Teacher";
    }

    public boolean equals(Teacher t){
        return teacherID.equals(t.getTeacherID());
    }

    public boolean isEmpty(){
        return teacherID.equals(Globals.EMPTY_TEACHER_ID);
    }

    public double getTeachingPointsAverage(){
        return teachingPointsAverage;
    }

    public void setTeachingPointsAverage(){
        teachingPointsAverage = (((double)teachingPoints) / (100 * maxPerWeek));
    }

    public void resetTeachingPointsAverage(){
        teachingPointsAverage=0;
    }

    public boolean isElligibleToTeach(int day){
            return teachingPoints < maxPerWeek && teachingPointsPerDay.get(day) < maxPerDay.get(day);

    }

    public boolean canTeachLesson(Lesson lesson){
        for(int i=0;i<lessonsTaught.size();i++){
            if(lessonsTaught.get(i).equals(lesson.getLessonID())){
                return true;
            }
        }
        return false;
    }

    public void increaseTeachingPoints(){
        teachingPoints++;
    }

    public void increaseTeachingPointsAtDay(int day){
        teachingPointsPerDay.set(day,teachingPointsPerDay.get(day) + 1);
    }

    public void resetTeachingPoints(){
        teachingPoints=0;
    }

    public void resetTeachingPointsPerDay(){
        for(int i=0;i< teachingPointsPerDay.size();i++){
            teachingPointsPerDay.set(i,0);
        }
    }

    public int getTeachingPoints(){
        return teachingPoints;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public ArrayList<String> getLessonsTaught() {
        return lessonsTaught;
    }

    public ArrayList<Integer> getMaxPerDay() {
        return maxPerDay;
    }
	
	public int getMaxPerWeek() {
		return maxPerWeek;
	}

}
