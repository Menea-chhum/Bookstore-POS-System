package model;

public class book {
    private int bookId;
    private String title;
    private String author;
    private double price;
    private int stockQuantity;
    private int categoryId;
    private int supplierId;

//    private String categoryName;  // for display only
//    private String supplierName;

    private String categoryName;
    private String supplierName;

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

    public void setBookId(int bookId){
        this.bookId = bookId;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setStockQuantity(int stockQuantity){
        this.stockQuantity = stockQuantity;
    }
    public void setCategoryId(int categoryId){
        this.categoryId = categoryId;
    }
    public void setSupplierId(int supplierId){
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }


}