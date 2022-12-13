package task;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MonthlyTask extends Task{
    public MonthlyTask(String title, String description, LocalDateTime taskDateTime, TaskType taskType) {
        super(title, description, taskDateTime, taskType);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        LocalDate taskDate = this.getTaskDateTime().toLocalDate();
//        return taskDate.equals(localDate) ||
//                (taskDate.isBefore(localDate) && taskDate.getDayOfMonth() == localDate.getDayOfMonth());
          return taskDate.equals(localDate) ||
                  (localDate.isAfter(taskDate) && taskDate.getDayOfMonth() == localDate.getDayOfMonth());
          //методы возвращают целые числа, поэтому ==
    }

    @Override
    public Repeatability getRepeatabilityType() {
        return Repeatability.MONTHLY;
    }
}
