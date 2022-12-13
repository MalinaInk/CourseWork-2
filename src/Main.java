import task.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;

import static task.TaskType.PERSONAL;
import static task.TaskType.WORK;

public class Main {
    private static final Schedule SCHEDULE = new Schedule();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH.mm");

    public static void main(String[] args) {

//        Scanner scanner = new Scanner(System.in);
//        SCHEDULE.addTask(new SingleTask("SingleTest", "Test", LocalDateTime.now(), WORK));
//        SCHEDULE.addTask(new DailyTask("DailyTest", "Test", LocalDateTime.now().plusHours(1), PERSONAL));
//        SCHEDULE.addTask(new WeeklyTask("WeeklyTest", "Test", LocalDateTime.now().plusHours(2), WORK));
//        SCHEDULE.addTask(new MonthlyTask("MonthlyTest", "Test", LocalDateTime.now(), PERSONAL));
//        SCHEDULE.addTask(new YearlyTask("YearlyTest", "Test", LocalDateTime.now(), WORK));
//        addTask(scanner);
//        printTaskForDate(scanner);
//        removeTasks(scanner);
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                printMenu();
                System.out.print("Выберите пункт меню: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            addTask(scanner);
                            break;
                        case 2:
                            removeTasks(scanner);
                            break;
                        case 3:
                            printTaskForDate(scanner);
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Выберите пункт меню из списка!");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. Добавить задачу");
        System.out.println("2. Удалить задачу");
        System.out.println("3. Получить задачу на указанный день");
        System.out.println("0. Выход");
    }


    private static void addTask(Scanner scanner) {
        String title = readString("Введите название задачи", scanner);
        String description = readString("Введите описание задачи", scanner);
        LocalDateTime taskDate = readDateTime(scanner);
        TaskType taskType = readType(scanner);
        Repeatability repeatability = readRepeatability(scanner);
        Task task = null;
        switch(repeatability){
            case SINGLE: task = new SingleTask(title,description,taskDate,taskType);
                break;
            case DAILY: task = new DailyTask(title,description,taskDate,taskType);
                break;
            case WEEKLY: task = new WeeklyTask(title,description,taskDate,taskType);
                break;
            case MONTHLY: task = new MonthlyTask(title,description,taskDate,taskType);
                break;
            case YEARLY: task = new YearlyTask(title,description,taskDate,taskType);
                break;
        };
        SCHEDULE.addTask(task);
    }
    private static Repeatability readRepeatability(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип повторяемости задачи");
                for (Repeatability repeatability : Repeatability.values()) {
                    System.out.println(repeatability.ordinal() + ". " + localizeRepeatability(repeatability));
                }
                System.out.print("Введите номер типа задачи:");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return Repeatability.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный номер типа задачи");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип задачи не найден");
            }
        }
    }

    private static TaskType readType(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип задачи");
                for (TaskType taskType : TaskType.values()) {
                    System.out.println(taskType.ordinal() + ". " + localizeType(taskType));
                }
                System.out.print("Введите номер типа задачи:");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return TaskType.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный номер типа задачи");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип задачи не найден");
            }
        }
    }

    private static LocalDateTime readDateTime(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        LocalTime localTime = readTime(scanner);
        return localDate.atTime(localTime);
    }

    private static String readString(String message, Scanner scanner) {
        while (true) {
            System.out.println(message);
            String readString = scanner.nextLine();
            if (readString == null || readString.isBlank()) {
                System.out.println("Введено пустое значение!");
            } else {
                return readString;
            }
        }
    }

    private static LocalTime readTime(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите время задачи в формате hh.mm: ");
                String dateLine = scanner.nextLine();
                return LocalTime.parse(dateLine, TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Введена дата в неверном формате");
            }
        }
    }

    private static void removeTasks(Scanner scanner) {
        System.out.println("Все задачи");
        for (Task task : SCHEDULE.getAllTasks()) {
            System.out.printf("%d. %s [%s](%s)%n",
                    task.getId(),
                    task.getTitle(),
                    localizeType(task.getTaskType()),
                    localizeRepeatability(task.getRepeatabilityType()));
        }
        while (true) {
            try {
                System.out.print("Выберите задачу для удаления: ");
                String idLine = scanner.nextLine();
                int id = Integer.parseInt(idLine);
                SCHEDULE.removeTask(id);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный id задачи");
            } catch (TaskNotFoundException e) {
                System.out.println("Задача для удаления не найдена");
            }
        }
    }
    private static void printTaskForDate(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        Collection<Task> taskForDate = SCHEDULE.getTaskForDate(localDate);
        System.out.println("Задачи на " + localDate.format(DATE_FORMAT));
        for (Task task : taskForDate) {
            System.out.printf("[%s]%s: %s (%s)%n",
                    localizeType(task.getTaskType()),
                    task.getTitle(),
                    task.getTaskDateTime().format(TIME_FORMAT),
                    task.getDescription());
        }
    }

    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите дату в формате dd.MM.yyyy: ");
                String dateLine = scanner.nextLine();
                return LocalDate.parse(dateLine, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Введена дата в неверном формате");
            }
        }
    }

    private static String localizeType(TaskType taskType) {
        String printTaskType = "Задача";
//        return switch (taskType) {
//            case WORK -> "Рабочая задача";
//            case PERSONAL -> "Персрнальная задача";
//        };
        switch (taskType) {
            case WORK:
                printTaskType = "Рабочая задача";
                break;
            case PERSONAL:
                printTaskType = "Персональная задача";
                break;
        }
        return printTaskType;
    }

    private static String localizeRepeatability(Repeatability repeatability) {
        String printLocalizeRepeatability = "задача";
        switch (repeatability) {
            case SINGLE:
                printLocalizeRepeatability = "Разовая";
                break;
            case DAILY:
                printLocalizeRepeatability = "Ежедневная";
                break;
            case WEEKLY:
                printLocalizeRepeatability = "Еженедельная";
                break;
            case MONTHLY:
                printLocalizeRepeatability = "Ежемесячная";
                break;
            case YEARLY:
                printLocalizeRepeatability = "Ежегодная";
                break;
        }
        return printLocalizeRepeatability;
    }
}
