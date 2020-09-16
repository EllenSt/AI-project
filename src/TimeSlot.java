public class TimeSlot {

    private Lesson currentLesson;
    private Teacher currentTeacher;

    public TimeSlot(){
        currentLesson = new Lesson();
        currentTeacher = new Teacher();
    }

    public TimeSlot(Lesson currentLesson, Teacher currentTeacher) {
        this.currentLesson = currentLesson;
        this.currentTeacher = currentTeacher;
    }

    public TimeSlot(TimeSlot old_slot) {
        this.currentLesson = old_slot.getLesson();
        this.currentTeacher = old_slot.getTeacher();
    }

    public boolean equals(TimeSlot comp){
        return  this.getLesson().equals(comp.getLesson());
    }

    public boolean isEmpty(){
        return currentLesson.isEmpty();
    }

    public Lesson getLesson() {
        return currentLesson;
    }

    public Teacher getTeacher() {
        return currentTeacher;
    }

    public void setCurrentLesson(Lesson currentLesson) {
        this.currentLesson = currentLesson;
    }

    public void setCurrentTeacherID(Teacher currentTeacher) {
        this.currentTeacher = currentTeacher;
    }

}
