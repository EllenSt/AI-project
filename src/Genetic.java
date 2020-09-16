import java.util.*;

public class Genetic {

    private ArrayList<Lesson> a_lessons;
    private ArrayList<Lesson> b_lessons;
    private ArrayList<Lesson> c_lessons;

    private ArrayList<Lesson>[] indexer;

    private ArrayList<Teacher> teachers;

    private ArrayList<Chromosome> vectorCollection;

    public Genetic(){
        a_lessons = new ArrayList<>();
        b_lessons = new ArrayList<>();
        c_lessons = new ArrayList<>();
        teachers = new ArrayList<>();
        indexer = new ArrayList[]{a_lessons,b_lessons,c_lessons};
        vectorCollection = new ArrayList<>(50);
        FileReader.readLessons(indexer);
        FileReader.readTeachers(teachers);
    }

    public boolean generateSchedule(){
        System.out.println("");
        System.out.print("Generating..");
        Globals.ARRAYLIST_SIZE = Globals.CLASSES * Globals.DAYS * Globals.HOURS;

        fillOriginalPopulation();

        int counter = 0;

        Random r = new Random();
        double prob1;
        double prob2;
        double msg_prob;
        int flag;
        Chromosome parent1 = null;
        Chromosome parent2= null;
        do{

            counter++;
            prob1 = r.nextDouble();
            parent1 = getParent(prob1);
            do{
                prob2 = r.nextDouble();
            }while(prob2==prob1);
            parent2 = getParent(prob2);
            createChildren(parent1,parent2);
            vectorCollection.sort(Comparator.comparing(Chromosome::calculateStrictFitness));
            if(counter>50){
                msg_prob = r.nextDouble();
                if(msg_prob < 0.3){
                    System.out.print("Wait for it...");
                }else if(msg_prob < 0.6){

                    System.out.print("Taking long huh?...");
                } else {
                    System.out.print("Hmm let's recast the spell...");
                }
                return false;
            }

        } while(vectorCollection.get(0).calculateStrictFitness() !=0);
        vectorCollection.get(0).printSlots();
        scheduleWriter.writeToTextFIle(vectorCollection.get(0));
        return countLessons(vectorCollection.get(0));
    }

    public void createChildren(Chromosome vec1,Chromosome vec2){
        System.out.print(".");
        Random rand = new Random();
        int v_index = rand.nextInt(Globals.DAYS * Globals.CLASSES/3 -1);

        Chromosome child2 = new Chromosome();
        Chromosome child1 = new Chromosome();

        createChild(v_index, child1, vec1, vec2);
        createChild(v_index, child2, vec2, vec1);


        if(countLessons(child2)){
            fillVectorWithTeachers(child2);
            vectorCollection.add(child2);
        }
        if(countLessons(child1)) {
            fillVectorWithTeachers(child1);
            vectorCollection.add( child1);
        }
        System.out.print("\r\r");

        vectorCollection.sort(Comparator.comparing(Chromosome::calculateStrictFitness));
        vectorCollection.subList(50, vectorCollection.size()).clear();

    }

    public void createChild(int index, Chromosome child, Chromosome _parent1, Chromosome _parent2 ){
        Chromosome parent1 = new Chromosome (_parent1);
        Chromosome parent2 = new Chromosome (_parent2);
        TimeSlot current_lesson;
        for(int x = 0 ; x <3*index ; x++){
            for(int i = 0;i<Globals.HOURS;i++){
                int cur_index = x  + i * Globals.CLASSES * Globals.DAYS;
                current_lesson = parent1.getGeneAt(cur_index);
                child.setGeneAt(cur_index,current_lesson);
            }
        }
        for(int l = 3*index ; l < Globals.CLASSES * Globals.DAYS; l+=3){

            int hours_counter =  Globals.HOURS;
            for(int ku = 0; ku < hours_counter;ku++){

                lol:
                for(int i = 3 * ((l/3)%3);i<Globals.CLASSES * Globals.DAYS;i+=9) {
                    for (int k = 0; k < Globals.HOURS; k++) {
                        int cur_index = i + k * Globals.CLASSES * Globals.DAYS;
                        current_lesson = parent2.getGeneAt(cur_index);

                        if( child.countNumberEqual(current_lesson.getLesson())<(current_lesson).getLesson().getWeeklyHoursTaught()){

                            child.setGeneAt(l + ku * Globals.CLASSES * Globals.DAYS,current_lesson);

                            int plus_index;
                            int minus_index;
                            int plus1_index;
                            int minus1_index;
                            int plus2_index;
                            int minus2_index;
                            //------------------------------------------------------------------------------------
                            int flag = 0;

                            if(!current_lesson.isEmpty()) {
                                for (int second_index = Globals.HOURS - 1; second_index >= 0; second_index--) {
                                    plus_index = l + second_index * Globals.CLASSES * Globals.DAYS;
                                    minus_index = l - second_index * Globals.CLASSES * Globals.DAYS;
                                    plus1_index = l + 1 + second_index * Globals.CLASSES * Globals.DAYS;
                                    minus1_index = l + 1 - second_index * Globals.CLASSES * Globals.DAYS;
                                    if (minus1_index < child.getGenesSize()
                                            && minus1_index >= 0
                                            && child.getGeneAt(minus1_index).isEmpty()
                                            && !(child.getGeneAt(minus_index).equals(current_lesson))
                                            ) {

                                        child.setGeneAt(minus1_index, current_lesson);
                                        flag = 1;
                                        break;

                                    } else if (plus1_index < child.getGenesSize()
                                            && child.getGeneAt(plus1_index).isEmpty()
                                            && !child.getGeneAt(plus_index).equals(current_lesson)) {

                                        child.setGeneAt(plus1_index, current_lesson);
                                        flag = 1;
                                        break;

                                    }
                                }
                                if (flag == 0) {

                                    child.setGeneAt(l + 1, current_lesson);
                                }

                                int flag1 = 0;
                                for (int second_index = Globals.HOURS - 1; second_index >= 0; second_index--) {
                                    plus1_index = l + 1 + second_index * Globals.CLASSES * Globals.DAYS;
                                    minus1_index = l + 1 - second_index * Globals.CLASSES * Globals.DAYS;
                                    plus2_index = l + 2 + second_index * Globals.CLASSES * Globals.DAYS;
                                    minus2_index = l + 2 - second_index * Globals.CLASSES * Globals.DAYS;

                                    if (minus2_index < child.getGenesSize()
                                            && minus2_index >= 0
                                            && child.getGeneAt(minus2_index).isEmpty()
                                            && !(child.getGeneAt(minus1_index).equals(current_lesson))) {

                                        child.setGeneAt(minus2_index, current_lesson);
                                        flag1 = 1;
                                        break;

                                    } else if (plus2_index < child.getGenesSize()
                                            && child.getGeneAt(plus2_index).isEmpty()
                                            && !child.getGeneAt(plus1_index).equals(current_lesson)) {

                                        child.setGeneAt(plus2_index, current_lesson);
                                        flag1 = 1;
                                        break;

                                    }
                                }
                                if (flag1 == 0) {
                                    child.setGeneAt(l + 2, current_lesson);
                                }
                            }

                            //------------------------------------------------------------------------------------

                            break lol;
                        }
                    }
                }
            }
        }
        while(child.calculateDispositionedEmpties()!=0){
            child.shiftEmptyLessonsToTheEndPerColumn();
        }
        mutate(child);

    }

    public void mutate(Chromosome newborn){
        Random r = new Random();
        int index1 = r.nextInt(Globals.DAYS * Globals.CLASSES/3 -1);
        int index2;
        do{
            index2 = r.nextInt(Globals.DAYS * Globals.CLASSES/3 -1);
        }while(index2!=index1);

        int gene_index1;
        int gene_index2;
        for(int i=0; i <= 2; i++){
            for(int k=0;k<Globals.HOURS;k++){
                gene_index1 = index1 + k*Globals.DAYS *Globals.CLASSES;
                gene_index2 = index2 + k*Globals.DAYS *Globals.CLASSES;
                newborn.swapGenes(gene_index1,gene_index2);
            }
        }

    }

    public void fillOriginalPopulation(){
        Chromosome vec1 = new Chromosome();
        int loop_counter;
        for(int i=0; i < 50;i++){
            loop_counter =0;
            do {
                vec1 = new Chromosome();
                loop_counter++;
                if(loop_counter>=50){
                    vec1 = new Chromosome();
                }
                fillInitialVectorWithLessons(vec1);
            }while(!countLessons(vec1));

            fillVectorWithTeachers(vec1);
            vectorCollection.add(vec1);
        }
    }

    public void fillInitialVectorWithLessons(Chromosome toBeFilled){
        ArrayList<Lesson> asource = (ArrayList) indexer[0].clone();
        ArrayList<Lesson> bsource = (ArrayList) indexer[1].clone();
        ArrayList<Lesson> csource = (ArrayList) indexer[2].clone();

        Random rand = new Random();

        ArrayList<Lesson>[] indexer = new ArrayList[]{asource,bsource,csource};


        int lesson_index;
        int plus_index;
        int minus_index;
        int plus1_index;
        int minus1_index;
        int plus2_index;
        int minus2_index;
        TimeSlot current_lesson;

        int i = 0;
        while (i<toBeFilled.getGenesSize() &&(indexer[0].size()>0 || indexer[1].size()>0 || indexer[2].size()>0  )){
            if(indexer[(i/3) % 3].size()>0){
                lesson_index = rand.nextInt(indexer[(i/3) % 3].size());
                current_lesson = new TimeSlot(indexer[(i/3) % 3].get(lesson_index), new Teacher());

                int flag = 0;
                if (toBeFilled.countNumberEqual(current_lesson.getLesson())<(current_lesson).getLesson().getWeeklyHoursTaught()){

                    toBeFilled.setGeneAt(i,current_lesson);

                    for(int second_index=Globals.HOURS-1;second_index>=0;second_index--){
                        plus_index = i + second_index * Globals.CLASSES * Globals.DAYS;
                        minus_index = i - second_index * Globals.CLASSES * Globals.DAYS;
                        plus1_index = i + 1 + second_index * Globals.CLASSES * Globals.DAYS;
                        minus1_index = i + 1 - second_index * Globals.CLASSES * Globals.DAYS;
                        if(minus1_index < toBeFilled.getGenesSize()
                                &&  minus1_index >=0
                                && toBeFilled.getGeneAt(minus1_index).isEmpty()
                                && !(toBeFilled.getGeneAt(minus_index).equals(current_lesson))) {

                            toBeFilled.setGeneAt(minus1_index, current_lesson);
                            flag =1;
                            break;

                        } else if(plus1_index < toBeFilled.getGenesSize()  ) {
                            if (toBeFilled.getGeneAt(plus1_index).isEmpty()
                                    && !toBeFilled.getGeneAt(plus_index).equals(current_lesson)) {

                                toBeFilled.setGeneAt(plus1_index, current_lesson);
                                flag =1;
                                break;
                            }
                        }
                    }
                    if(flag==0){
                        toBeFilled.setGeneAt(i+1, current_lesson);
                    }
                    int flag1 = 0;
                    for(int second_index=0;second_index<=Globals.HOURS;second_index++){
                        plus1_index = i + 1 + second_index * Globals.CLASSES * Globals.DAYS;
                        minus1_index = i + 1 - second_index * Globals.CLASSES * Globals.DAYS;
                        plus2_index = i + 2 + second_index * Globals.CLASSES * Globals.DAYS;
                        minus2_index = i + 2 - second_index * Globals.CLASSES * Globals.DAYS;

                        if(minus2_index < toBeFilled.getGenesSize()
                                &&  minus2_index >=0
                                && toBeFilled.getGeneAt(minus2_index).isEmpty()
                                && !(toBeFilled.getGeneAt(minus1_index).equals(current_lesson))) {

                            toBeFilled.setGeneAt(minus2_index, current_lesson);
                            flag1 =1;
                            break;

                        } else if(i+2 + second_index*Globals.CLASSES * Globals.DAYS < toBeFilled.getGenesSize()  ) {
                            if (toBeFilled.getGeneAt(plus2_index).isEmpty()
                                    && !toBeFilled.getGeneAt(plus1_index).equals(current_lesson)) {

                                toBeFilled.setGeneAt(plus2_index, current_lesson);
                                flag1 =1;
                                break;
                            }
                        }
                    }
                    if(flag1==0){
                        toBeFilled.setGeneAt(i+2, current_lesson);
                    }
                    i+=3;
                } else if(toBeFilled.countNumberEqual(current_lesson.getLesson())==(current_lesson).getLesson().getWeeklyHoursTaught()){
                    indexer[(i/3)% 3].remove(lesson_index);
                }
            } else {
                toBeFilled.setGeneAt(i,new TimeSlot(new Lesson(), new Teacher()));
                i+=3;
            }
        }
    }

    public void fillVectorWithTeachers(Chromosome toBeFilled){

        int index;
        for(int i = 0; i <Globals.DAYS;i++){
            for(int u=i*Globals.CLASSES; u < (i+1)*Globals.CLASSES; u++){
                for(int k = 0; k < Globals.HOURS; k++ ){
                    index = u + k*Globals.DAYS*Globals.CLASSES;
                    if(!toBeFilled.getGeneAt(index).isEmpty()){
                        Teacher teach_found = getFirstTeacher(toBeFilled.getGeneAt(index).getLesson(), teachers,i);
                        if(!(teach_found.getTeacherID()==Globals.EMPTY_TEACHER_ID)){

                            toBeFilled.getGeneAt(index).setCurrentTeacherID(teach_found);
                            teach_found.increaseTeachingPoints();
                            teach_found.increaseTeachingPointsAtDay(i);
                        }
                    } else {
                        toBeFilled.getGeneAt(index).setCurrentTeacherID(new Teacher());
                    }
                    teachers.sort(Comparator.comparing(Teacher::getTeachingPoints));
                }
            }
        }

        teachers.forEach((lol)->{
            lol.resetTeachingPoints();
            lol.resetTeachingPointsPerDay();
        });
    }

    public Chromosome getParent(double prob){
        Chromosome found = null;
        do{
            Random rand = new Random();
            int ind = rand.nextInt(vectorCollection.size());
            if(vectorCollection.get(ind).getProbabity()<= prob){
                found  = vectorCollection.get(ind);
            }
        }while(found==null);
        return found;
    }

    public Teacher getFirstTeacher(Lesson lesson, ArrayList<Teacher> teachers, int day){
        for(int i=0;i<teachers.size();i++){
            Teacher t = teachers.get(i);
                if(t.canTeachLesson(lesson) && t.isElligibleToTeach(day)){
                    return  t;
                }

        }
        return new Teacher();
    }

    public boolean countLessons(Chromosome chromo){
        for(int i=0;i<indexer.length;i+=2){
            for(int y=0;y<indexer[i].size();y++){
                if(!(chromo.numberEqual(indexer[i].get(y))==3*indexer[i].get(y).getWeeklyHoursTaught())){
                    return false;
                }
            }
        }
        return true;
    }

}
