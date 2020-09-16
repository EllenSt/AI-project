import java.util.HashMap;
import java.util.ArrayList;

public class Chromosome {
	private ArrayList<TimeSlot> genes;
    private int hardScore;
    private double probability;
	
	public Chromosome() {
		this.genes = new ArrayList(Globals.ARRAYLIST_SIZE);
        initializeGenes();
	}

	public Chromosome(Chromosome old_chromo) {
        this.genes = new ArrayList(Globals.ARRAYLIST_SIZE);
        deepCopyFrom(old_chromo);
	}

	public void initializeGenes(){
        for(int i = 0; i<Globals.ARRAYLIST_SIZE; i++){
            genes.add(new TimeSlot(new Lesson(), new Teacher()));
        }
    }

    public void setProbability(){
	    probability = 1 / (double) calculateStrictFitness();
    }

    public double getProbabity(){
        return probability;
    }
	
	public ArrayList<TimeSlot> getGenes() {
		return this.genes;
	}

	public void setGenes(ArrayList Genes) {
		this.genes = Genes;
	}

    public int getHardScore() {
        calculateStrictFitness();
        return hardScore;
    }

    public int calculateDispositionedEmpties(){
        int score = 0;

        for(int i=0;i<Globals.DAYS*Globals.CLASSES;i++){
            int flag = 1;
            int blank_count = 0;

            for(int u=0;u<Globals.HOURS;u++){
                if(!(genes.get(i + u*Globals.DAYS*Globals.CLASSES)).isEmpty() && flag ==1){
                    continue;
                }else if((genes.get(i + u*Globals.DAYS*Globals.CLASSES)).isEmpty()){
                    flag = 0;
                    blank_count++;
                } else if(!(genes.get(i + u*Globals.DAYS*Globals.CLASSES)).isEmpty() && flag==0){
                    score += blank_count;
                    flag = 1;
                }
            }
        }
        return  score;
    }

    public int assessScheduleUniformity(){
        int scoreStrict = 0;
        int previous_hours_count = 6;
        int current_hours_count;
        int row_index;
        for(int i=0; i<Globals.CLASSES*Globals.DAYS;i++ ){
            current_hours_count = 0;
            for(int k=0;k<Globals.HOURS; k++){
                row_index = i + k*Globals.CLASSES*Globals.DAYS;
                if(!genes.get(row_index).isEmpty()){
                    current_hours_count++;
                }
            }

            if(Math.abs(current_hours_count - previous_hours_count)>2){
                scoreStrict+=Math.abs(current_hours_count - previous_hours_count);
            }
            previous_hours_count = current_hours_count;
        }
        return scoreStrict;
    }

    public int assessTeachersHoursUniformity(){
        int scoreStrict = 0;
        HashMap<String,Teacher> teachersMap = new HashMap<>();
        for(int i = 0; i < Globals.ARRAYLIST_SIZE; i++){
            if(genes.get(i).getTeacher().getTeacherID()!=null && !genes.get(i).getTeacher().isEmpty()){
                addTeachersToHashMap(genes.get(i).getTeacher(),teachersMap);
            }
        }
        double dif = 0.0;
        for(Teacher t : teachersMap.values()){

            for(Teacher s : teachersMap.values()){
                dif = Math.abs(t.getTeachingPointsAverage() - s.getTeachingPointsAverage());
                if(dif> 0.15){
                    scoreStrict++;
                }
            }
        }
        genes.forEach((lol)->{
            lol.getTeacher().resetTeachingPointsAverage();
            lol.getTeacher().resetTeachingPoints();
        });
        return scoreStrict;
    }

    public void addTeachersToHashMap(Teacher t , HashMap<String,Teacher> map){
            t.increaseTeachingPoints();
            t.setTeachingPointsAverage();
            map.put(t.getTeacherID(), t);

    }

    public int assessLessonsPerDayUniformity(){
        int scoreStrict = 0;
        int index;
        HashMap<String,Lesson> lessonsMap = new HashMap<>();
        for(int i=0;i<Globals.DAYS*Globals.CLASSES;i+=3){
            for(int k=0;k<Globals.HOURS;k++){
                index = i + k*Globals.DAYS*Globals.CLASSES;
                if(!genes.get(index).isEmpty()){
                    addLessonsToHashMap(genes.get(index).getLesson(),lessonsMap);
                }
            }

            double today_div;
            double max_div;

            for(Lesson t : lessonsMap.values()){
                today_div = t.getTodayCounter() / (double) Globals.DAYS;
                max_div = t.getWeeklyHoursTaught() / (double) Globals.DAYS;
                if(today_div>=max_div && t.getWeeklyHoursTaught()!=1){
                    scoreStrict++;
                }
            }
            lessonsMap.clear();
            genes.forEach((lol)->
                lol.getLesson().resetTodayCounter()
            );
        }
        return scoreStrict;
    }

    public void addLessonsToHashMap(Lesson l , HashMap<String,Lesson> map){
        l.increaseTodayCounter();
        map.put(l.getLessonID(), l);

    }

    public int findContinuousTeachingHours(){
        int scoreStrict = 0;
        ArrayList<TimeSlot> copiedGenes = (ArrayList<TimeSlot>) genes.clone();
        TimeSlot current_timeslot = null;
        int index;
        int index_in;
        int flag = 0;
        int spec_counter=0;
        for(int i=0;i<Globals.DAYS*Globals.CLASSES;i+=3){
            for(int t=0;t<Globals.HOURS;t++){
                for(int k=0;k<=2;k++){
                    index = i + k + t*Globals.DAYS*Globals.CLASSES;
                    if(!copiedGenes.get(index).isEmpty()){
                        current_timeslot = copiedGenes.get(index);
                        spec_counter=0;
                        internal_search:
                        for(int t_in=t+1;t_in<Globals.HOURS;t_in++){

                            for(int k_in=0;k_in<=2;k_in++){

                                index_in = i + k_in + t_in*Globals.DAYS*Globals.CLASSES;

                                if(current_timeslot.getTeacher().equals(copiedGenes.get(index_in).getTeacher())){
                                    spec_counter++;
                                    if(spec_counter>=3){
                                        scoreStrict++;
                                    }
                                    flag = 1;
                                    copiedGenes.set(index_in,new TimeSlot());
                                }
                                if(flag!=1){
                                    break internal_search;
                                } else {
                                    flag = 0;
                                }
                            }

                        }
                    }
                }
            }
        }
        return scoreStrict;
    }

    public int calculateStrictFitness() {
        int scoreStrict = 0;
        scoreStrict += calculateDispositionedEmpties();
        scoreStrict += assessScheduleUniformity();
        scoreStrict += assessTeachersHoursUniformity();
        scoreStrict += assessLessonsPerDayUniformity();
        scoreStrict += findContinuousTeachingHours();

        return scoreStrict;
    }

	public int getGenesSize(){
	    return genes.size();
    }

	public TimeSlot getGeneAt(int index){
	    return genes.get(index);
    }

    public void setGeneAt(int index, TimeSlot gene){
	    genes.set(index,gene);
    }

    public int countNumberEqual(Lesson lessonToCheck) {
        int count = 0;

        for(int i=0;i<genes.size();i=i+3){
            if(genes.get(i).getLesson().getLessonID().equals(lessonToCheck.getLessonID())){
                count++;
            }
        }
        return count;
    }

    public int numberEqual(Lesson lessonToCheck) {
        int mcount = 0;
        for (int oo= 0;oo<genes.size();oo++) {
            if (lessonToCheck.equals(genes.get(oo).getLesson())) {
                mcount ++;
            }
        }
        return mcount;
    }

	public void printSlots() {System.out.println();

        for(int i=0;i<genes.size();i++){
            System.out.print((genes.get(i)).getLesson().getLessonID() + " " + genes.get(i).getTeacher().getTeacherID() + " ") ;

            //System.out.print((genes.elementAt(i)).getLesson().getLessonID() + " ");
            if((i+1)%(Globals.DAYS*Globals.CLASSES)==0 && i!=0){
                System.out.println();
            }
        }
	}

	public void deepCopyFrom(Chromosome old_chromo){
	    for(int i = 0; i <old_chromo.getGenes().size();i++){
	        genes.add(i,new TimeSlot(old_chromo.getGenes().get(i)));
        }
    }

    public void swapGenes(int index1, int index2){
	    TimeSlot geneAtIndex1 = genes.get(index1);
        TimeSlot geneAtIndex2 = genes.get(index2);
        genes.set(index1, geneAtIndex2);
        genes.set(index2, geneAtIndex1);
    }

    public void shiftEmptyLessonsToTheEndPerColumn(){
        int index;
        for(int i = 0; i <Globals.DAYS*Globals.CLASSES;i++){
            for(int k = 0; k < Globals.HOURS; k++ ){
                index = i +k * Globals.DAYS*Globals.CLASSES;
                if (genes.get(index).isEmpty()){
                    for(int shift_index = k; shift_index < Globals.HOURS-1; shift_index++ ){
                        swapGenes(i +shift_index * Globals.DAYS*Globals.CLASSES,i +(shift_index+1) * Globals.DAYS*Globals.CLASSES);
                    }
                }
            }
        }
    }

}
