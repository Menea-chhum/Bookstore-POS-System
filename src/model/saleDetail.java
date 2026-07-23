package model;

public class saleDetail {
    private int detailId;
    private int saleId;
    private int bookId;
    private int quantitySold;
    private double unitPrice;

    public saleDetail(int detailId, int saleId, int bookId, int quantitySold, double unitPrice)
    {
        this.detailId = detailId;
        this.saleId = saleId;
        this.bookId = bookId;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
    }

    public int getDetailId()
    {
        return detailId;
    }
    public int getSaleId()
    {
        return saleId;
    }
    public int getBookId()
    {
        return bookId;
    }
    public int getQuantitySold()
    {
        return quantitySold;
    }
    public double getUnitPrice()
    {
        return unitPrice;
    }
}
