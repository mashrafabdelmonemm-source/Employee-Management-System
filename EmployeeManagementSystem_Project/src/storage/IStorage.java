package storage;

import java.util.List;

public interface IStorage<T> {
    java.util.List<T> load();
    void save(java.util.List<T> data);
}
