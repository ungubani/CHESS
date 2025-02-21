//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//public class DefaultPackageDiagramGenerator {
//    public static void main(String[] args) throws Exception {
//        File rootFolder = new File("C:\\Users\\admin\\Desktop\\Предметы\\Действующий семестр\\ТП\\Семестр_5\\Курсовая\\CHESS"); // Задаем корневую директорию
//        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
//            System.err.println("Указанная директория './CHESS' не существует или не является директорией.");
//            return;
//        }
//
//        StringBuilder plantUmlScript = new StringBuilder("@startuml\n");
//
//        // Генерируем классы и зависимости
//        processFolder(rootFolder, plantUmlScript);
//
//        plantUmlScript.append("@enduml");
//
//        // Выводим итоговый PlantUML скрипт
//        System.out.println(plantUmlScript);
//    }
//
//    // Метод для рекурсивной обработки всех подпапок и файлов
//    private static void processFolder(File folder, StringBuilder plantUmlScript) throws IOException {
//        File[] files = folder.listFiles();
//        if (files == null) return;
//
//        for (File file : files) {
//            if (file.isDirectory()) {
//                // Если текущий файл - папка, обрабатываем её рекурсивно
//                processFolder(file, plantUmlScript);
//            } else if (file.getName().endsWith(".java")) {
//                // Обрабатываем Java файл
//                processJavaFile(file, plantUmlScript);
//            }
//        }
//    }
//
//    // Метод для анализа и добавления классов и их зависимостей в PlantUML
//    private static void processJavaFile(File file, StringBuilder plantUmlScript) throws IOException {
//        String content = new String(Files.readAllBytes(Paths.get(file.getPath()))); // Читаем содержимое файла
//        String className = file.getName().replace(".java", "");
//
//        // Добавляем класс
//        plantUmlScript.append("class ").append(className).append(" {}\n");
//
//        // Ищем наследование (extends)
//        if (content.contains("extends")) {
//            String extendsClass = content.split("extends")[1].trim().split("[ \\{]")[0];
//            plantUmlScript.append(className).append(" --|> ").append(extendsClass).append("\n");
//        }
//
//        // Ищем реализацию интерфейсов (implements)
//        if (content.contains("implements")) {
//            String[] interfaces = content.split("implements")[1].trim().split("[ \\{]")[0].split(",");
//            for (String implementedInterface : interfaces) {
//                plantUmlScript.append(className).append(" ..|> ").append(implementedInterface.trim()).append("\n");
//            }
//        }
//    }
//}


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultPackageDiagramGenerator {

    public static void main(String[] args) throws Exception {
        File rootFolder = new File("C:\\Users\\admin\\Desktop\\Предметы\\Действующий семестр\\ТП\\Семестр_5\\Курсовая\\CHESS"); // Задаем корневую директорию
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            System.err.println("Указанная директория './CHESS' не существует или не является директорией.");
            return;
        }

        StringBuilder plantUmlScript = new StringBuilder("@startuml\n");

        // Генерируем классы, методы и зависимости
        processFolder(rootFolder, plantUmlScript);

        plantUmlScript.append("@enduml");

        // Выводим итоговый PlantUML скрипт
        System.out.println(plantUmlScript);
    }

    // Метод для рекурсивной обработки всех подпапок и файлов
    private static void processFolder(File folder, StringBuilder plantUmlScript) throws IOException {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                // Если текущий файл - папка, обрабатываем её рекурсивно
                processFolder(file, plantUmlScript);
            } else if (file.getName().endsWith(".java")) {
                // Обрабатываем Java файл
                processJavaFile(file, plantUmlScript);
            }
        }
    }

    // Метод для анализа и добавления классов, методов и их связей в PlantUML
    private static void processJavaFile(File file, StringBuilder plantUmlScript) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(file.getPath()))); // Читаем содержимое файла
        String className = file.getName().replace(".java", "");

        // Добавляем класс
        plantUmlScript.append("class ").append(className).append(" {\n");

        // Извлекаем методы и добавляем их в диаграмму
        extractMethods(content, plantUmlScript);

        plantUmlScript.append("}\n");

        // Ищем наследование (extends)
        if (content.contains("extends")) {
            String extendsClass = content.split("extends")[1].trim().split("[ \\{]")[0];
            plantUmlScript.append(className).append(" --|> ").append(extendsClass).append("\n");
        }

        // Ищем реализацию интерфейсов (implements)
        if (content.contains("implements")) {
            String[] interfaces = content.split("implements")[1].trim().split("[ \\{]")[0].split(",");
            for (String implementedInterface : interfaces) {
                plantUmlScript.append(className).append(" ..|> ").append(implementedInterface.trim()).append("\n");
            }
        }
    }

    // Извлечение методов из содержимого
    private static void extractMethods(String content, StringBuilder plantUmlScript) {
        // Регулярное выражение для поиска методов (публичные, защищённые, приватные)
        Pattern methodPattern = Pattern.compile("(public|protected|private)\\s+\\w+\\s+(\\w+)\\s*\\(([^)]*)\\)");
        Matcher matcher = methodPattern.matcher(content);
        while (matcher.find()) {
            String visibility = matcher.group(1); // public/private/protected
            String methodName = matcher.group(2); // Имя метода
            String parameters = matcher.group(3); // Параметры метода

            // Переводим модификаторы доступа в PlantUML
            String umlVisibility = visibility.equals("public") ? "+" : visibility.equals("protected") ? "#" : "-";

            // Добавляем метод в UML-представление
            plantUmlScript.append("    ").append(umlVisibility).append(methodName).append("(").append(parameters).append(")\n");
        }
    }
}
