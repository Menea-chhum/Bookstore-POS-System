package model;

public class book {
    private int bookId;
    private String title;
    private String author;
    private double price;
    private int stockQuantity;
    private int categoryId;
    private int supplierId;

    public book(int bookId, String title,
                String author, double price,
                int stockQuantity, int categoryId,
                int supplierId) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
    }

    public int getBookId()
    {
        return bookId;
    }
    public String getTitle()
    {
        return title;
    }
    public String getAuthor()
    {
        return author;
    }
    public double getPrice()
    {
        return price;
    }
    public int getStockQuantity()
    {
        return stockQuantity;
    }
    public int getCategoryId()
    {
        return categoryId;
    }
    public int getSupplierId()
    {
        return supplierId;
    }
}