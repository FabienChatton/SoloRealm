package ch.solorealm;

import ch.solorealm.beans.ingredient.IngredientPair;
import ch.solorealm.beans.machine.MachineNode;
import ch.solorealm.beans.machine.MachineProcessRecipe;
import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RecipeWrk {
    private Map<Class<MachineNode>, Collection<MachineProcessRecipe>> recipeMap;

    public void loadMachines() throws Exception {
        List<Class<MachineNode>> machineNodeClass = getMachineNodeClass();
        recipeMap = generateRecipeMap(machineNodeClass);
    }

    public Map<Class<MachineNode>, Collection<MachineProcessRecipe>> getMatchRecipe(IngredientPair ingredientPair) {
        Map<Class<MachineNode>, Collection<MachineProcessRecipe>> map = new HashMap<>();
        for (Class<MachineNode> machineNodeClass : recipeMap.keySet()) {
            for (MachineProcessRecipe machineProcessRecipe : recipeMap.get(machineNodeClass)) {
                if (machineProcessRecipe.output() != null) {
                    if (machineProcessRecipe.output().equals(ingredientPair)) {
                        map.putIfAbsent(machineNodeClass, new ArrayList<>());
                        map.get(machineNodeClass).add(machineProcessRecipe);
                    }
                }
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private List<Class<MachineNode>> getMachineNodeClass() throws IOException, ClassNotFoundException {
        String packageName = "ch.solorealm.beans.machine";
        List<Class<MachineNode>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource != null && resource.getProtocol().equals("jar")) {
            JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
            try (JarFile jarFile = jarURLConnection.getJarFile()) {
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();

                    if (entryName.startsWith(path) && entryName.endsWith(".class") && !entry.isDirectory()) {
                        String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                        Class<?> machineClass = Class.forName(className);
                        if (MachineNode.class.isAssignableFrom(machineClass)) {
                            try {
                                machineClass.getConstructor();
                            } catch (NoSuchMethodException e) {
                                continue;
                            }
                            classes.add((Class<MachineNode>) machineClass);
                        }
                    }
                }
            }
        }
        return classes;
    }

    private Map<Class<MachineNode>, Collection<MachineProcessRecipe>> generateRecipeMap(List<Class<MachineNode>> machineClasses) {
        Map<Class<MachineNode>, Collection<MachineProcessRecipe>> map = new HashMap<>();
        for (Class<MachineNode> machineClass : machineClasses) {
            try {
                MachineNode machineNode = machineClass.getConstructor().newInstance();
                map.put(machineClass, machineNode.machineProcessRecipes);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                Gdx.app.log("Recipe", "Cannot load recipe from \"" + machineClass.getSimpleName() + "\" machine");
            }
        }
        return map;
    }
}
