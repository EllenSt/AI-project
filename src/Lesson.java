public class Lesson {

    private String lessonID;
    private String lessonName;
    private int yearTaught;
    private int weeklyHoursTaught;
    private int todayCounter;

    public Lesson(){
        lessonID = Globals.EMPTY_LESSON_ID;
        lessonName = "Empty";
        yearTaught = 0;
        weeklyHoursTaught = 0;
        todayCounter = 0;
    }

    public Lesson(String id, String name, int year, int hours){
        lessonID = id;
        lessonName = name;
        yearTaught = year;
        weeklyHoursTaught = hours;
        todayCounter = 0;
    }

    public Lesson(Lesson old){
        lessonID = old.lessonID;
        lessonName = old.lessonName;
        yearTaught = old.yearTaught;
        weeklyHoursTaught = old.weeklyHoursTaught;
        todayCounter = old.todayCounter;
    }

    public void increaseTodayCounter(){
        todayCounter++;
    }

    public int getTodayCounter(){
        return todayCounter;
    }

    public void resetTodayCounter(){
        todayCounter = 0;
    }

    public boolean equals(Lesson comp){
        return  this.lessonID.equals(comp.getLessonID());
    }

    public void setEmpty(){
        lessonID = Globals.EMPTY_LESSON_ID;
        lessonName = "Empty";
        yearTaught = 0;
        weeklyHoursTaught = 0;
    }

    public boolean isEmpty(){
        return lessonID.equals(Globals.EMPTY_LESSON_ID);
    }

    public String getLessonID() {
        return lessonID;
    }

    public String getLessonName() {
        return lessonName;
    }

    public int getYearTaught() {
        return yearTaught;
    }

    public int getWeeklyHoursTaught() {
        return weeklyHoursTaught;
    }
}
