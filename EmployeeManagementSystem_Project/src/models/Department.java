package models;

import java.util.Objects;

/**
 * Department model - small tweaks: validation in setters, equals/hashCode on id
 */
public class Department {
    private int id;
    private String name;
    private String description;

    public Department() {}

    public Department(int id, String name, String description) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("id must be non-negative");
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        String t = name.trim();
        if (t.isEmpty()) throw new IllegalArgumentException("name cannot be empty");
        this.name = t;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = (description == null) ? "" : description.trim();
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

