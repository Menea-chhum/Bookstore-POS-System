package model;

public class category {
    private int categoryId;
    private String categoryName;
    private String description;

    public category(int categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId; }
    public String getCategoryName() {
        return categoryName; }
    public String getDescription() {
        return description; }
}