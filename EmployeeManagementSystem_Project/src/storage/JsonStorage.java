package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.Writer;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonStorage - improvements:
 * - guard against getParent() == null
 * - atomic save: write to temp file then move
 * - return empty list on error but print stack for debugging
 */
public class JsonStorage<T> implements IStorage<T> {

    private final String filePath;
    private final Gson gson;
    private final Type listType;

    public JsonStorage(String filePath, Type listType) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.listType = listType;

        try {
            Path p = Paths.get(filePath);
            Path parent = p.getParent();
            if (parent != null) {
                if (!Files.exists(parent)) {
                    Files.createDirectories(parent);
                }
            }
            if (!Files.exists(p)) {
                Files.write(p, "[]".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> load() {
        try (Reader reader = new FileReader(filePath)) {
            List<T> data = gson.fromJson(reader, listType);
            return (data != null) ? data : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void save(List<T> data) {
        // atomic write: write to temp file then move
        Path p = Paths.get(filePath);
        Path tmp = Paths.get(filePath + ".tmp");
        try (Writer writer = new FileWriter(tmp.toFile())) {
            gson.toJson(data, listType, writer);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            Files.move(tmp, p, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            // fallback: try non-atomic replace
            try {
                Files.move(tmp, p, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

