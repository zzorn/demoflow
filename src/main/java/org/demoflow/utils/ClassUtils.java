package org.demoflow.utils;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Class related utility functions.
 */
public final class ClassUtils {

    /**
     * @param rootPath root path that all packages are located under.
     * @param type type that the classes should implement or extend.
     * @param packageToGetClassesFrom package to look for classes implementing the type in.
     * @return all classes that implement or extend the specified type, and are located in the specified package.
     */
    public static <T> List<Class<? extends T>> getClassesImplementing(String rootPath,
                                                                      final Class<T> type,
                                                                      String packageToGetClassesFrom) {
        return getClassesImplementing(rootPath, type, Collections.singletonList(packageToGetClassesFrom));
    }

    /**
     * @param rootPath root path that all packages are located under.
     * @param type type that the classes should implement or extend.
     * @param packagesToGetClassesFrom packages to look for classes implementing the type in.
     * @return all classes that implement or extend the specified type, and are located in the specified package.
     */
    public static <T> List<Class<? extends T>> getClassesImplementing(String rootPath,
                                                                      final Class<T> type,
                                                                      List<String> packagesToGetClassesFrom) {
        final List<Class<? extends T>> classes = new ArrayList<>();

        Reflections reflections = new Reflections(rootPath);
        final Set<Class<? extends T>> allSubTypes = reflections.getSubTypesOf(type);
        for (Class<? extends T> component : allSubTypes) {
            // Get the subtypes that are in the specified packages
            for (String packageToGetClassesFrom : packagesToGetClassesFrom) {
                if (component.getPackage().getName().equals(packageToGetClassesFrom)) {
                    classes.add(component);
                }
            }
        }

        return classes;
    }


    private ClassUtils() {
    }

}
